import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "./ui/dialog";
import { Badge } from "./ui/badge";
import { Progress } from "./ui/progress";
import { TrendingUp, Users, MapPin } from "lucide-react";
import type { Vote } from "./VotingCard";

interface StatsModalProps {
  vote: Vote | null;
  isOpen: boolean;
  onClose: () => void;
}

export function StatsModal({ vote, isOpen, onClose }: StatsModalProps) {
  if (!vote) return null;

  const getPercentage = (votes: number) => {
    return vote.totalVotes > 0 ? Math.round((votes / vote.totalVotes) * 100) : 0;
  };

  const demographicData = [
    { age: "16세", percentage: 18, gender: "여성" },
    { age: "17세", percentage: 25, gender: "남성" },
    { age: "18세", percentage: 22, gender: "여성" },
    { age: "19세", percentage: 35, gender: "여성" },
  ];

  const relatedCategories = ["아이돌", "패션", "음식", "게임"];

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-lg max-h-[80vh] overflow-y-auto bg-[#181818] border-white/10 text-white">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-white">
            <TrendingUp className="w-5 h-5 text-[#1DB954]" />
            투표 분석 리포트
          </DialogTitle>
        </DialogHeader>

        <div className="space-y-6">
          {/* Vote Title */}
          <div>
            <h3 className="mb-2 text-white">{vote.title}</h3>
            <div className="flex items-center gap-2">
              <Badge variant="secondary" className="bg-white/10 text-white border-0">{vote.category}</Badge>
              <div className="flex items-center gap-1 text-sm text-muted-foreground">
                <Users className="w-4 h-4" />
                <span>{vote.totalVotes.toLocaleString()}명 참여</span>
              </div>
            </div>
          </div>

          {/* Results */}
          <div>
            <h4 className="mb-3 text-white">📊 투표 결과</h4>
            <div className="space-y-3">
              {vote.options.map((option, index) => {
                const percentage = getPercentage(option.votes);
                const isWinner = option.votes === Math.max(...vote.options.map(o => o.votes));
                
                return (
                  <div key={option.id} className="space-y-2">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2 text-white">
                        {option.emoji && <span className="text-lg">{option.emoji}</span>}
                        <span>{option.text}</span>
                        {isWinner && (
                          <Badge className="bg-[#1DB954] text-black border-0">
                            👑 1위
                          </Badge>
                        )}
                      </div>
                      <span className="text-[#1DB954]">{percentage}%</span>
                    </div>
                    <Progress value={percentage} className="h-2 bg-white/10" />
                    <div className="text-xs text-muted-foreground">
                      {option.votes.toLocaleString()}명 투표
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Demographics */}
          <div>
            <h4 className="mb-3 text-white">👥 참여자 분석</h4>
            <div className="bg-white/5 rounded-lg p-4 space-y-3 border border-white/10">
              <div className="flex items-center justify-between">
                <span className="text-sm text-white">가장 많이 참여한 연령대</span>
                <Badge className="bg-[#1DB954] text-black">19세 여성 35%</Badge>
              </div>
              
              <div className="space-y-2">
                {demographicData.map((data, index) => (
                  <div key={index} className="flex items-center justify-between text-sm">
                    <span className="text-white">{data.age} {data.gender}</span>
                    <div className="flex items-center gap-2 flex-1 max-w-[150px] ml-4">
                      <Progress value={data.percentage} className="h-1.5 bg-white/10" />
                      <span className="text-xs text-muted-foreground w-8">{data.percentage}%</span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>

          {/* Geographic */}
          {vote.schoolName && (
            <div>
              <h4 className="mb-3 text-white">📍 지역 분석</h4>
              <div className="bg-[#1DB954]/10 rounded-lg p-4 border border-[#1DB954]/20">
                <div className="flex items-center gap-2 mb-2">
                  <MapPin className="w-4 h-4 text-[#1DB954]" />
                  <span className="text-sm text-white">이 투표는 <strong>{vote.schoolName}</strong>에서 시작되었습니다</span>
                </div>
                <p className="text-xs text-muted-foreground">
                  같은 학교 학생 중 73%가 참여했어요
                </p>
              </div>
            </div>
          )}

          {/* Related Interests */}
          <div>
            <h4 className="mb-3 text-white">🔗 관련 관심사</h4>
            <p className="text-sm text-muted-foreground mb-3">
              이 투표에 참여한 사람들은 다음 카테고리에도 관심이 많아요
            </p>
            <div className="flex flex-wrap gap-2">
              {relatedCategories.map((category) => (
                <Badge key={category} variant="outline" className="hover:bg-white/5 bg-white/5 text-white border-white/10">
                  {category}
                </Badge>
              ))}
            </div>
          </div>

          {/* Fun Fact */}
          <div className="bg-[#1DB954]/10 rounded-lg p-4 border border-[#1DB954]/20">
            <div className="text-2xl mb-2">💡 재미있는 사실</div>
            <p className="text-sm text-white">
              이 투표는 {vote.timeLeft ? "지금 진행 중이며" : "종료되었고"}, 
              시간당 평균 {Math.round(vote.totalVotes / 3)}명이 참여하고 있어요!
              {vote.isHot && " 현재 가장 핫한 투표 중 하나예요 🔥"}
            </p>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
}
