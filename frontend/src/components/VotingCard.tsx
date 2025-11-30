import { useState, useRef } from "react";
import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Progress } from "./ui/progress";
import { TrendingUp, Users, Clock, ChevronRight, Archive } from "lucide-react";
import { ImageWithFallback } from "./figma/ImageWithFallback";
import { motion, AnimatePresence } from "motion/react";

export type VoteType = "balance" | "multiple" | "ox";

export interface VoteOption {
  id: string;
  text?: string;
  image?: string;
  votes: number;
  emoji?: string;
}

export interface Vote {
  id: string;
  type: VoteType;
  title: string;
  description?: string;
  options: VoteOption[];
  totalVotes: number;
  category: string;
  isHot?: boolean;
  timeLeft?: string;
  points?: number;
  userVoted?: string | null;
  schoolName?: string;
}

interface VotingCardProps {
  vote: Vote;
  onVote: (voteId: string, optionId: string) => void;
  onViewStats: (vote: Vote) => void;
}

export function VotingCard({ vote, onVote, onViewStats }: VotingCardProps) {
  const [selectedOption, setSelectedOption] = useState<string | null>(
    vote.userVoted || null
  );
  const [hasVoted, setHasVoted] = useState(!!vote.userVoted);
  const [isAnimating, setIsAnimating] = useState(false);
  const [animatingOption, setAnimatingOption] = useState<VoteOption | null>(null);
  const [animationPositions, setAnimationPositions] = useState({ 
    startX: 0, 
    startY: 0, 
    endX: 0, 
    endY: 0 
  });
  const ballotBoxRef = useRef<HTMLDivElement>(null);

  const handleVote = (optionId: string, optionData: VoteOption, event: React.MouseEvent) => {
    if (hasVoted) return;
    
    // Get absolute positions
    const buttonRect = event.currentTarget.getBoundingClientRect();
    const ballotRect = ballotBoxRef.current?.getBoundingClientRect();
    
    if (ballotRect) {
      // Calculate center of button (start position)
      const startX = buttonRect.left + buttonRect.width / 2;
      const startY = buttonRect.top + buttonRect.height / 2;
      
      // Calculate center of ballot box (end position)
      const endX = ballotRect.left + ballotRect.width / 2;
      const endY = ballotRect.top + ballotRect.height / 2;
      
      setAnimationPositions({
        startX,
        startY,
        endX,
        endY
      });
    }
    
    setIsAnimating(true);
    setSelectedOption(optionId);
    setAnimatingOption(optionData);
    
    setTimeout(() => {
      setHasVoted(true);
      onVote(vote.id, optionId);
    }, 1200);
    
    setTimeout(() => {
      setIsAnimating(false);
      setAnimatingOption(null);
    }, 1400);
  };

  const getPercentage = (votes: number) => {
    return vote.totalVotes > 0 ? Math.round((votes / vote.totalVotes) * 100) : 0;
  };

  // Category-based color schemes
  const getCategoryColors = (category: string) => {
    const colors: Record<string, { from: string; to: string; accent: string }> = {
      "음식": { from: "#f97316", to: "#ef4444", accent: "#f97316" },
      "패션": { from: "#8b5cf6", to: "#ec4899", accent: "#8b5cf6" },
      "게임": { from: "#3b82f6", to: "#14b8a6", accent: "#3b82f6" },
      "일상": { from: "#eab308", to: "#f97316", accent: "#eab308" },
      "학교": { from: "#3b82f6", to: "#8b5cf6", accent: "#3b82f6" },
      "아이돌": { from: "#ec4899", to: "#8b5cf6", accent: "#ec4899" },
      "영화/드라마": { from: "#ef4444", to: "#8b5cf6", accent: "#ef4444" },
      "운동": { from: "#14b8a6", to: "#3b82f6", accent: "#14b8a6" },
      "취미": { from: "#8b5cf6", to: "#3b82f6", accent: "#8b5cf6" },
      "밈/유머": { from: "#ec4899", to: "#f97316", accent: "#ec4899" },
      "환경": { from: "#10b981", to: "#84cc16", accent: "#10b981" },
    };
    return colors[category] || { from: "#1DB954", to: "#1aa34a", accent: "#1DB954" };
  };

  const categoryColors = getCategoryColors(vote.category);

  return (
    <Card className="overflow-visible bg-card hover:bg-white/5 transition-all duration-300 border-white/10 relative group">
      {/* Gradient Accent Bar */}
      <div 
        className="absolute top-0 left-0 right-0 h-1"
        style={{
          background: `linear-gradient(to right, ${categoryColors.from}, ${categoryColors.to})`
        }}
      />
      
      {/* Ballot Box */}
      <div 
        ref={ballotBoxRef}
        className="absolute top-4 right-4 z-20"
      >
        <motion.div
          animate={isAnimating ? { scale: [1, 1.2, 1], rotate: [0, -5, 5, 0] } : {}}
          transition={{ duration: 0.5 }}
          className="relative"
        >
          <div 
            className="w-12 h-12 rounded-lg flex items-center justify-center"
            style={{
              background: `linear-gradient(135deg, ${categoryColors.from}, ${categoryColors.to})`,
              boxShadow: isAnimating ? '0 0 20px rgba(29, 185, 84, 0.5)' : 'none'
            }}
          >
            <Archive className="w-6 h-6 text-black" />
          </div>
          {isAnimating && (
            <motion.div
              className="absolute inset-0 rounded-lg"
              style={{
                background: `linear-gradient(135deg, ${categoryColors.from}, ${categoryColors.to})`,
              }}
              animate={{ scale: [1, 1.5], opacity: [0.5, 0] }}
              transition={{ duration: 0.6, repeat: 2 }}
            />
          )}
        </motion.div>
      </div>

      {/* Flying Paper Animation */}
      <AnimatePresence>
        {isAnimating && animatingOption && (
          <motion.div
            className="fixed pointer-events-none z-[100]"
            initial={{ 
              left: animationPositions.startX,
              top: animationPositions.startY,
              x: '-50%',
              y: '-50%',
              scale: 1,
              opacity: 1,
              rotateX: 0,
              rotateY: 0,
              rotateZ: 0,
            }}
            animate={{
              left: animationPositions.endX,
              top: animationPositions.endY,
              scale: [1, 0.9, 0.4, 0.1],
              rotateX: [0, 30, 60, 120],
              rotateY: [0, 10, 20, 30],
              rotateZ: [0, -10, 10, -5],
              opacity: [1, 1, 0.8, 0],
            }}
            transition={{
              duration: 1.2,
              ease: [0.43, 0.13, 0.23, 0.96],
            }}
            style={{
              transformStyle: 'preserve-3d',
              perspective: '1000px',
            }}
          >
            <div 
              className="p-6 rounded-lg border-2 shadow-2xl backdrop-blur-sm min-w-[120px]"
              style={{
                background: `linear-gradient(135deg, ${categoryColors.from}40, ${categoryColors.to}40)`,
                borderColor: categoryColors.accent,
                boxShadow: `0 10px 40px ${categoryColors.accent}60`,
              }}
            >
              {animatingOption.emoji && (
                <div className="text-4xl text-center mb-2">{animatingOption.emoji}</div>
              )}
              <div className="text-white text-center font-semibold">{animatingOption.text}</div>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
      
      {/* Header */}
      <div className="p-4 pb-3">
        <div className="flex items-start justify-between mb-2">
          <div className="flex items-center gap-2 flex-wrap">
            <Badge
              className="border-0"
              style={{
                background: `linear-gradient(135deg, ${categoryColors.from}, ${categoryColors.to})`,
                color: '#000'
              }}
            >
              {vote.category}
            </Badge>
            {vote.isHot && (
              <Badge className="gap-1 bg-[#1DB954] hover:bg-[#1ED760] text-black border-0 animate-pulse">
                <TrendingUp className="w-3 h-3" />
                HOT
              </Badge>
            )}
            {vote.points && (
              <Badge 
                variant="outline" 
                className="border-0"
                style={{
                  backgroundColor: `${categoryColors.accent}20`,
                  borderColor: `${categoryColors.accent}40`,
                  color: categoryColors.accent
                }}
              >
                +{vote.points}P
              </Badge>
            )}
          </div>
          {vote.timeLeft && (
            <div className="flex items-center gap-1 text-muted-foreground">
              <Clock className="w-3 h-3" />
              <span className="text-xs">{vote.timeLeft}</span>
            </div>
          )}
        </div>
        
        <h3 className="mb-1 text-white">{vote.title}</h3>
        {vote.description && (
          <p className="text-sm text-muted-foreground">{vote.description}</p>
        )}
        
        <div className="flex items-center gap-3 mt-3 text-sm text-muted-foreground">
          <div className="flex items-center gap-1">
            <Users className="w-4 h-4" />
            <span>{vote.totalVotes.toLocaleString()}명 참여</span>
          </div>
          {vote.schoolName && (
            <div className="text-xs bg-white/5 px-2 py-1 rounded">
              📍 {vote.schoolName}
            </div>
          )}
        </div>
      </div>

      {/* Options */}
      <div className="p-4">
        {vote.type === "balance" && (
          <div className="grid grid-cols-2 gap-3">
            {vote.options.map((option) => {
              const percentage = getPercentage(option.votes);
              const isSelected = selectedOption === option.id;
              const isWinning = hasVoted && option.votes === Math.max(...vote.options.map(o => o.votes));
              
              return (
                <button
                  key={option.id}
                  onClick={(e) => handleVote(option.id, option, e)}
                  disabled={hasVoted}
                  className={`relative p-4 rounded-lg border transition-all duration-300 ${
                    hasVoted
                      ? isSelected
                        ? "bg-white/5"
                        : "border-white/10 bg-white/5"
                      : "border-white/20 hover:bg-white/5 active:scale-95"
                  } ${isAnimating && isSelected ? "opacity-50" : ""}`}
                  style={hasVoted && isSelected ? {
                    borderColor: categoryColors.accent,
                    backgroundColor: `${categoryColors.accent}10`
                  } : hasVoted ? {} : {
                    borderColor: 'rgba(255,255,255,0.2)'
                  }}
                >
                  {hasVoted && (
                    <div
                      className="absolute inset-0 rounded-lg"
                      style={{ 
                        width: `${percentage}%`,
                        background: `linear-gradient(90deg, ${categoryColors.accent}30, ${categoryColors.accent}10)`
                      }}
                    />
                  )}
                  <div className="relative z-10">
                    {option.image && (
                      <div className="mb-2 rounded-lg overflow-hidden">
                        <ImageWithFallback
                          src={option.image}
                          alt={option.text || "Option"}
                          className="w-full h-24 object-cover"
                        />
                      </div>
                    )}
                    {option.emoji && <div className="text-3xl mb-2">{option.emoji}</div>}
                    <div className="mb-2 text-white">{option.text}</div>
                    {hasVoted && (
                      <div className="flex items-center justify-between mt-2">
                        <span style={{ color: categoryColors.accent }}>{percentage}%</span>
                        {isWinning && (
                          <Badge 
                            className="text-xs border-0"
                            style={{
                              background: `linear-gradient(135deg, ${categoryColors.from}, ${categoryColors.to})`,
                              color: '#000'
                            }}
                          >
                            우세
                          </Badge>
                        )}
                      </div>
                    )}
                  </div>
                </button>
              );
            })}
          </div>
        )}



        {vote.type === "multiple" && (
          <div className="space-y-2">
            {vote.options.map((option) => {
              const percentage = getPercentage(option.votes);
              const isSelected = selectedOption === option.id;
              
              return (
                <button
                  key={option.id}
                  onClick={(e) => handleVote(option.id, option, e)}
                  disabled={hasVoted}
                  className={`w-full p-4 rounded-lg border transition-all duration-300 text-left ${
                    hasVoted
                      ? isSelected
                        ? ""
                        : "border-white/10 bg-white/5"
                      : "border-white/20 hover:bg-white/5 active:scale-[0.98]"
                  } ${isAnimating && isSelected ? "opacity-50" : ""}`}
                  style={hasVoted && isSelected ? {
                    borderColor: categoryColors.accent,
                    backgroundColor: `${categoryColors.accent}10`
                  } : {}}
                >
                  {option.image && (
                    <div className="mb-3 rounded-lg overflow-hidden">
                      <ImageWithFallback
                        src={option.image}
                        alt={option.text || "Option"}
                        className="w-full h-32 object-cover"
                      />
                    </div>
                  )}
                  <div className="flex items-center justify-between mb-2">
                    <span className="flex items-center gap-2 text-white">
                      {option.emoji && <span className="text-xl">{option.emoji}</span>}
                      <span>{option.text}</span>
                    </span>
                    {hasVoted && <span style={{ color: categoryColors.accent }}>{percentage}%</span>}
                  </div>
                  {hasVoted && (
                    <div className="relative h-2 bg-white/10 rounded-full overflow-hidden">
                      <div 
                        className="absolute inset-y-0 left-0 rounded-full transition-all"
                        style={{
                          width: `${percentage}%`,
                          background: `linear-gradient(90deg, ${categoryColors.from}, ${categoryColors.to})`
                        }}
                      />
                    </div>
                  )}
                </button>
              );
            })}
          </div>
        )}

        {vote.type === "ox" && (
          <div className="grid grid-cols-2 gap-4">
            {vote.options.map((option) => {
              const percentage = getPercentage(option.votes);
              const isSelected = selectedOption === option.id;
              const isO = option.text === "O";
              
              return (
                <button
                  key={option.id}
                  onClick={(e) => handleVote(option.id, option, e)}
                  disabled={hasVoted}
                  className={`relative p-8 rounded-lg border transition-all duration-300 ${
                    hasVoted
                      ? isSelected
                        ? ""
                        : "border-white/10 bg-white/5"
                      : "border-white/20 hover:bg-white/5"
                  } active:scale-95 ${isAnimating && isSelected ? "opacity-50" : ""}`}
                  style={hasVoted && isSelected ? {
                    borderColor: categoryColors.accent,
                    backgroundColor: `${categoryColors.accent}10`
                  } : {}}
                >
                  <div 
                    className="text-6xl text-center"
                    style={{
                      color: isO ? "#3b82f6" : "#ef4444"
                    }}
                  >
                    {option.text}
                  </div>
                  {hasVoted && (
                    <div className="mt-4 text-center">
                      <div 
                        className="text-2xl"
                        style={{ color: categoryColors.accent }}
                      >
                        {percentage}%
                      </div>
                    </div>
                  )}
                </button>
              );
            })}
          </div>
        )}

        {hasVoted && (
          <Button
            variant="ghost"
            className="w-full mt-4 gap-2 hover:bg-white/5 text-muted-foreground hover:text-white"
            onClick={() => onViewStats(vote)}
          >
            상세 통계 보기
            <ChevronRight className="w-4 h-4" />
          </Button>
        )}
      </div>
    </Card>
  );
}
