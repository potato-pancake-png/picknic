import { Card } from "./ui/card";
import { Button } from "./ui/button";
import { Badge } from "./ui/badge";
import { Progress } from "./ui/progress";
import {
  Gift,
  Zap,
  Calendar,
  TrendingUp,
  Award,
  Settings,
  LogOut,
} from "lucide-react";
import { Avatar, AvatarFallback } from "./ui/avatar";

interface ProfileSectionProps {
  userPoints: number;
  userRank: number;
  verifiedSchool: string | null;
  onRewardClick: () => void;
  onLogout: () => void;
}

export function ProfileSection({
  userPoints,
  userRank,
  verifiedSchool,
  onRewardClick,
  onLogout,
}: ProfileSectionProps) {
  const stats = [
    { label: "참여한 투표", value: 142, icon: "📊" },
    { label: "만든 투표", value: 28, icon: "✨" },
    { label: "출석일", value: 23, icon: "📅" },
    { label: "정답률", value: "68%", icon: "🎯" },
  ];

  const recentActivities = [
    { type: "vote", text: "평생 떡볶이만 vs 햄버거만", points: 1, time: "5분 전" },
    { type: "create", text: "투표 생성: 오늘 뭐 먹지?", points: 2, time: "1시간 전" },
    { type: "win", text: "정답 맞춤! (발로란트 우승팀)", points: 10, time: "3시간 전" },
    { type: "daily", text: "출석 체크", points: 5, time: "오늘" },
  ];

  const achievements = [
    { emoji: "🔥", name: "7일 연속 출석", unlocked: true },
    { emoji: "💯", name: "투표 100회 참여", unlocked: true },
    { emoji: "🎯", name: "정답률 70%", unlocked: false },
    { emoji: "👑", name: "투표왕", unlocked: false },
  ];

  return (
    <div className="space-y-6">
      {/* Profile Header */}
      <Card className="p-6 bg-gradient-to-br from-[#3b82f6] via-[#8b5cf6] to-[#ec4899] text-white border-0 relative overflow-hidden">
        <div className="absolute inset-0 bg-black/10" />
        <div className="flex items-start justify-between mb-4 relative z-10">
          <div className="flex items-center gap-4">
            <Avatar className="w-16 h-16 border-4 border-white/20">
              <AvatarFallback className="bg-white/20 text-white text-xl backdrop-blur-sm">
                지
              </AvatarFallback>
            </Avatar>
            <div>
              <h2 className="text-xl mb-1 text-white">지민</h2>
              <div className="flex items-center gap-2">
                <Badge className="bg-white/20 text-white border-0 backdrop-blur-sm">
                  🥈 실버 레벨
                </Badge>
                {verifiedSchool && (
                  <Badge className="bg-lime-500/20 text-lime-300 border-0 backdrop-blur-sm">
                    🏫 {verifiedSchool}
                  </Badge>
                )}
              </div>
            </div>
          </div>
          <Button
            variant="ghost"
            size="icon"
            className="text-white hover:bg-white/20 relative z-10"
          >
            <Settings className="w-5 h-5" />
          </Button>
        </div>

        <div className="grid grid-cols-2 gap-3 relative z-10">
          <div className="bg-white/10 rounded-lg p-3 backdrop-blur-sm">
            <div className="flex items-center gap-2 text-sm text-white/80">
              <Zap className="w-4 h-4" />
              <span>포인트</span>
            </div>
            <div className="text-2xl mt-1 text-white">{userPoints}P</div>
          </div>
          <div className="bg-white/10 rounded-lg p-3 backdrop-blur-sm">
            <div className="flex items-center gap-2 text-sm text-white/80">
              <TrendingUp className="w-4 h-4" />
              <span>랭킹</span>
            </div>
            <div className="text-2xl mt-1 text-white">#{userRank}</div>
          </div>
        </div>

        <div className="mt-4 space-y-2 relative z-10">
          <div className="flex items-center justify-between text-sm">
            <span className="text-white/80">다음 레벨까지</span>
            <span className="text-white">230P 남음</span>
          </div>
          <Progress value={75} className="h-2 bg-white/20" />
        </div>
      </Card>

      {/* Quick Actions */}
      <div className="grid grid-cols-2 gap-3">
        <Button
          onClick={onRewardClick}
          className="h-auto py-4 flex-col gap-2 border-0 bg-gradient-to-br from-[#f97316] to-[#ef4444] hover:from-[#fb923c] hover:to-[#f87171] text-white"
        >
          <Gift className="w-6 h-6" />
          <span>보상 받기</span>
        </Button>
        <Button
          variant="outline"
          className="h-auto py-4 flex-col gap-2 border-white/10 bg-gradient-to-br from-[#14b8a6]/10 to-[#3b82f6]/10 text-white hover:from-[#14b8a6]/20 hover:to-[#3b82f6]/20"
        >
          <Calendar className="w-6 h-6" />
          <span>출석 체크</span>
          <Badge variant="secondary" className="text-xs bg-[#14b8a6] text-black border-0">
            +5P
          </Badge>
        </Button>
      </div>

      {/* Stats */}
      <Card className="p-5 bg-card border-white/10">
        <h3 className="mb-4 flex items-center gap-2 text-white">
          <Award className="w-5 h-5 text-[#1DB954]" />
          활동 통계
        </h3>
        <div className="grid grid-cols-2 gap-4">
          {stats.map((stat) => (
            <div
              key={stat.label}
              className="bg-white/5 rounded-lg p-3 border border-white/10"
            >
              <div className="text-2xl mb-1">{stat.icon}</div>
              <div className="text-2xl mb-1 text-white">{stat.value}</div>
              <div className="text-xs text-muted-foreground">{stat.label}</div>
            </div>
          ))}
        </div>
      </Card>

      {/* Recent Activities */}
      <Card className="p-5 bg-card border-white/10">
        <h3 className="mb-4 text-white">최근 활동</h3>
        <div className="space-y-3">
          {recentActivities.map((activity, index) => (
            <div
              key={index}
              className="flex items-start gap-3 pb-3 border-b border-white/10 last:border-0"
            >
              <div className="w-8 h-8 rounded-full bg-white/5 flex items-center justify-center shrink-0 text-sm">
                {activity.type === "vote" && "📊"}
                {activity.type === "create" && "✨"}
                {activity.type === "win" && "🎉"}
                {activity.type === "daily" && "📅"}
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm truncate text-white">{activity.text}</p>
                <p className="text-xs text-muted-foreground mt-0.5">
                  {activity.time}
                </p>
              </div>
              <Badge
                variant="secondary"
                className="shrink-0 bg-[#1DB954]/20 text-[#1DB954] border-0"
              >
                +{activity.points}P
              </Badge>
            </div>
          ))}
        </div>
      </Card>

      {/* Achievements */}
      <Card className="p-5 bg-card border-white/10">
        <h3 className="mb-4 text-white">업적</h3>
        <div className="grid grid-cols-2 gap-3">
          {achievements.map((achievement, index) => (
            <div
              key={index}
              className={`p-4 rounded-lg border text-center transition-all ${
                achievement.unlocked
                  ? "border-[#1DB954]/30 bg-[#1DB954]/10"
                  : "border-white/10 bg-white/5 opacity-50"
              }`}
            >
              <div
                className={`text-3xl mb-2 ${
                  !achievement.unlocked && "grayscale"
                }`}
              >
                {achievement.emoji}
              </div>
              <p className="text-xs text-white">{achievement.name}</p>
            </div>
          ))}
        </div>
      </Card>

      {/* Settings */}
      <Card className="p-4 bg-card border-white/10">
        <div className="space-y-2">
          <Button variant="ghost" className="w-full justify-start gap-2 text-white hover:bg-white/5">
            <Settings className="w-4 h-4" />
            설정
          </Button>
          <Button 
            variant="ghost" 
            className="w-full justify-start gap-2 text-red-400 hover:text-red-300 hover:bg-red-500/10"
            onClick={onLogout}
          >
            <LogOut className="w-4 h-4" />
            로그아웃
          </Button>
        </div>
      </Card>
    </div>
  );
}
