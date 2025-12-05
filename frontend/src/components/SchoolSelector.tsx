import { useState, useEffect } from 'react';
import { Check, ChevronsUpDown } from 'lucide-react';
import { Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList } from './ui/command';
import { Popover, PopoverContent, PopoverTrigger } from './ui/popover';
import { Button } from './ui/button';
import { Label } from './ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from './ui/select';
import { cn } from './ui/utils';
import { type School, schoolService } from '../services/schoolService';

interface SchoolSelectorProps {
  value: string;
  onChange: (schoolName: string) => void;
  className?: string;
}

export function SchoolSelector({ value, onChange, className }: SchoolSelectorProps) {
  const [schools, setSchools] = useState<School[]>([]);
  const [filteredSchools, setFilteredSchools] = useState<School[]>([]);

  const [selectedType, setSelectedType] = useState<'HIGH' | 'MIDDLE' | 'all'>('all');
  const [searchTerm, setSearchTerm] = useState('');

  const [open, setOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // 성능 최적화: 렌더링할 최대 학교 수
  const MAX_DISPLAY_SCHOOLS = 100;

  // Load schools on mount
  useEffect(() => {
    const loadSchools = async () => {
      try {
        setIsLoading(true);
        const data = await schoolService.getAllSchools();
        setSchools(data);
        setFilteredSchools(data);
        setError(null);
      } catch (err) {
        setError('학교 목록을 불러오는데 실패했습니다.');
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };

    loadSchools();
  }, []);

  // Apply filters whenever filter criteria change
  useEffect(() => {
    if (schools.length === 0) return;

    const filtered = schoolService.filterSchools(schools, {
      type: selectedType === 'all' ? undefined : selectedType,
      searchTerm: searchTerm,
    });

    setFilteredSchools(filtered);
  }, [schools, selectedType, searchTerm]);

  // Reset filters
  const handleResetFilters = () => {
    setSelectedType('all');
    setSearchTerm('');
  };

  const hasActiveFilters = selectedType !== 'all';

  // 성능 최적화: 표시할 학교 목록 제한
  const displaySchools = filteredSchools.slice(0, MAX_DISPLAY_SCHOOLS);
  const hasMoreSchools = filteredSchools.length > MAX_DISPLAY_SCHOOLS;

  return (
    <div className={cn('space-y-3', className)}>
      {/* Filter Controls */}
      <div className="space-y-2">
        <div className="flex items-center justify-between">
          <Label className="text-white/80 text-sm">학교 검색</Label>
          {hasActiveFilters && (
            <Button
              type="button"
              variant="ghost"
              size="sm"
              onClick={handleResetFilters}
              className="h-6 px-2 text-xs text-lime-400 hover:text-lime-300"
            >
              필터 초기화
            </Button>
          )}
        </div>

        {/* Type Filter */}
        <Select value={selectedType} onValueChange={(val) => setSelectedType(val as any)}>
          <SelectTrigger className="h-9 bg-white/5 border-white/10 text-white text-sm rounded-lg">
            <SelectValue placeholder="학교 구분" />
          </SelectTrigger>
          <SelectContent className="bg-black border-white/50 text-white">
            <SelectItem value="all">전체</SelectItem>
            <SelectItem value="HIGH">고등학교</SelectItem>
            <SelectItem value="MIDDLE">중학교</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {/* School Selection with Command */}
      <div className="space-y-1.5">
        <Popover open={open} onOpenChange={setOpen}>
          <PopoverTrigger asChild>
            <Button
              variant="outline"
              role="combobox"
              aria-expanded={open}
              className="w-full h-11 justify-between bg-white/5 border-white/10 text-white hover:bg-white/10 hover:text-white rounded-lg"
              disabled={isLoading}
            >
              {isLoading ? (
                <span className="text-white/40">로딩 중...</span>
              ) : value ? (
                <span>{value}</span>
              ) : (
                <span className="text-white/40">학교를 선택하세요</span>
              )}
              <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0 bg-black border-white/50" align="start">
            <Command className="bg-black text-white">
              <CommandInput
                placeholder="학교명으로 검색..."
                value={searchTerm}
                onValueChange={setSearchTerm}
                className="text-white placeholder:text-white/40"
              />
              <CommandList>
                <CommandEmpty>
                  {error ? (
                    <span className="text-red-400">{error}</span>
                  ) : (
                    <span className="text-white/40">검색 결과가 없습니다</span>
                  )}
                </CommandEmpty>
                <CommandGroup>
                  {displaySchools.map((school) => (
                    <CommandItem
                      key={school.id}
                      value={school.name}
                      onSelect={(currentValue) => {
                        onChange(currentValue === value ? '' : currentValue);
                        setOpen(false);
                      }}
                      className="cursor-pointer text-white hover:bg-lime-500/30 data-[selected=true]:bg-lime-500/50"
                    >
                      <Check
                        className={cn(
                          'mr-2 h-4 w-4',
                          value === school.name ? 'opacity-100 text-lime-400' : 'opacity-0'
                        )}
                      />
                      <div className="flex flex-col">
                        <span className="font-medium">{school.name}</span>
                        <span className="text-xs text-white/40">
                          {school.type === 'HIGH' ? '고등학교' : '중학교'}
                        </span>
                      </div>
                    </CommandItem>
                  ))}
                </CommandGroup>
              </CommandList>
            </Command>
          </PopoverContent>
        </Popover>
      </div>

      {error && !isLoading && (
        <p className="text-xs text-red-400 mt-1">{error}</p>
      )}
    </div>
  );
}
