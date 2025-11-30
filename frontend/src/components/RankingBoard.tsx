import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import { Trophy, TrendingUp, Award, Medal } from "lucide-react";
import { Avatar, AvatarFallback } from "./ui/avatar";
import { Progress } from "./ui/progress";

interface RankingBoardProps {
  userPoints: number;
  userRank: number;
}

export function RankingBoard({ userPoints, userRank }: RankingBoardProps) {
  const personalRankings = [
    { rank: 1, name: "투표왕", points: 2850, level: "🏆 마스터", avatar: "투" },
    { rank: 2, name: "밸런스의달인", points: 2620, level: "💎 다이아", avatar: "밸" },
    { rank: 3, name: "민초단", points: 2340, level: "💎 다이아", avatar: "민" },
    { rank: 4, name: "깻잎논쟁러", points: 2180, level: "🥇 골드", avatar: "깻" },
    { rank: 5, name: "투표중독", points: 1950, level: "🥇 골드", avatar: "투" },
    { rank: 6, name: "지민", points: userPoints, level: "🥈 실버", avatar: "지", isUser: true },
    { rank: 7, name: "선택장애", points: 1620, level: "🥈 실버", avatar: "선" },
    { rank: 8, name: "핫이슈러", points: 1480, level: "🥈 실버", avatar: "핫" },
  ];

  const schoolRankings = [
    { rank: 1, school: "서울고등학교", points: 45280, members: 342, avatar: "서" },
    { rank: 2, school: "강남고등학교", points: 42150, members: 318, avatar: "강" },
    { rank: 3, school: "부산여고", points: 38920, members: 295, avatar: "부" },
    { rank: 4, school: "대구고등학교", points: 35600, members: 267, avatar: "대" },
    { rank: 5, school: "인천고등학교", points: 32450, members: 241, avatar: "인" },
    { rank: 6, school: "광주고등학교", points: 29800, members: 228, avatar: "광" },
    { rank: 7, school: "대전고등학교", points: 27350, members: 215, avatar: "대" },
  ];

  const getRankBadgeColor = (rank: number) => {
    if (rank === 1) return "bg-[#1DB954] text-black";
    if (rank === 2) return "bg-white/20 text-white";
    if (rank === 3) return "bg-orange-500/80 text-white";
    return "bg-white/10 text-muted-foreground";
  };

  const getRankIcon = (rank: number) => {
    if (rank === 1) return <Trophy className="w-5 h-5" />;
    if (rank === 2) return <Medal className="w-5 h-5" />;
    if (rank === 3) return <Award className="w-5 h-5" />;
    return <span>{rank}</span>;
  };

  return (
    <div className="space-y-6">
      {/* User Stats */}
      <Card className="p-6 bg-gradient-to-br from-[#8b5cf6] via-[#ec4899] to-[#f97316] text-white border-0 relative overflow-hidden">
        <div className="absolute inset-0 bg-black/10" />
        <div className="flex items-center justify-between mb-4 relative z-10">
          <div>
            <p className="text-white/80 text-sm">내 랭킹</p>
            <div className="flex items-center gap-2 mt-1">
              <span className="text-4xl">#{userRank}</span>
              <Badge className="bg-white/20 text-white border-0 backdrop-blur-sm">🥈 실버</Badge>
            </div>
          </div>
          <div className="text-right">
            <p className="text-white/80 text-sm">보유 포인트</p>
            <div className="text-3xl mt-1">{userPoints}P</div>
          </div>
        </div>

        <div className="space-y-2 relative z-10">
          <div className="flex items-center justify-between text-sm">
            <span className="text-white/80">다음 등급까지</span>
            <span className="text-white">230P 남음</span>
          </div>
          <Progress value={75} className="h-2 bg-white/20" />
        </div>
      </Card>

      {/* Rankings Tabs */}
      <Tabs defaultValue="personal" className="w-full">
        <TabsList className="grid w-full grid-cols-2 bg-card border border-white/10">
          <TabsTrigger value="personal" className="gap-2 data-[state=active]:bg-[#1DB954] data-[state=active]:text-black">
            <TrendingUp className="w-4 h-4" />
            개인 랭킹
          </TabsTrigger>
          <TabsTrigger value="school" className="gap-2 data-[state=active]:bg-[#1DB954] data-[state=active]:text-black">
            <Trophy className="w-4 h-4" />
            학교 랭킹
          </TabsTrigger>
        </TabsList>

        <TabsContent value="personal" className="space-y-2 mt-4">
          {personalRankings.map((user) => (
            <Card
              key={user.rank}
              className={`p-4 transition-all border-white/10 ${
                user.isUser
                  ? "border-2 border-[#1DB954] bg-[#1DB954]/10"
                  : "bg-card hover:bg-[#1f1f1f]"
              }`}
            >
              <div className="flex items-center gap-4">
                <Badge
                  className={`w-10 h-10 rounded-full flex items-center justify-center shrink-0 ${getRankBadgeColor(
                    user.rank
                  )}`}
                >
                  {getRankIcon(user.rank)}
                </Badge>

                <Avatar className="w-12 h-12">
                  <AvatarFallback
                    className={user.isUser ? "bg-[#1DB954] text-black" : "bg-white/10 text-white"}
                  >
                    {user.avatar}
                  </AvatarFallback>
                </Avatar>

                <div className="flex-1">
                  <div className="flex items-center gap-2">
                    <span className={`text-white ${user.isUser ? "font-medium" : ""}`}>
                      {user.name}
                    </span>
                    {user.isUser && (
                      <Badge variant="secondary" className="text-xs bg-[#1DB954] text-black border-0">
                        나
                      </Badge>
                    )}
                  </div>
                  <div className="flex items-center gap-2 mt-1">
                    <Badge variant="outline" className="text-xs bg-white/5 border-white/10 text-white">
                      {user.level}
                    </Badge>
                    <span className="text-sm text-muted-foreground">
                      {user.points.toLocaleString()}P
                    </span>
                  </div>
                </div>

                {user.rank <= 3 && !user.isUser && (
                  <div className="text-2xl">
                    {user.rank === 1 ? "🏆" : user.rank === 2 ? "🥈" : "🥉"}
                  </div>
                )}
              </div>
            </Card>
          ))}
        </TabsContent>

        <TabsContent value="school" className="space-y-2 mt-4">
          {schoolRankings.map((school) => (
            <Card key={school.rank} className="p-4 bg-card hover:bg-[#1f1f1f] transition-all border-white/10">
              <div className="flex items-center gap-4">
                <Badge
                  className={`w-10 h-10 rounded-full flex items-center justify-center shrink-0 ${getRankBadgeColor(
                    school.rank
                  )}`}
                >
                  {getRankIcon(school.rank)}
                </Badge>

                <Avatar className="w-12 h-12">
                  <AvatarFallback className="bg-gradient-to-br from-[#1DB954] to-[#1aa34a] text-black">
                    {school.avatar}
                  </AvatarFallback>
                </Avatar>

                <div className="flex-1">
                  <div className="text-white">{school.school}</div>
                  <div className="flex items-center gap-3 mt-1 text-sm text-muted-foreground">
                    <span>👥 {school.members}명</span>
                    <span>• {school.points.toLocaleString()}P</span>
                  </div>
                </div>

                {school.rank === 1 && (
                  <Badge className="bg-[#1DB954] text-black border-0">
                    🔥 1위
                  </Badge>
                )}
              </div>
            </Card>
          ))}

          <Card className="p-4 bg-card border-2 border-dashed border-white/20">
            <div className="text-center space-y-2">
              <div className="text-2xl">🏫</div>
              <p className="text-sm text-white">우리 학교도 랭킹에 도전하세요!</p>
              <p className="text-xs text-muted-foreground">
                학교 인증 후 친구들과 함께 포인트를 모아보세요
              </p>
            </div>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Rewards Info */}
      <Card className="p-4 bg-[#1DB954]/10 border border-[#1DB954]/20">
        <div className="flex items-start gap-3">
          <div className="text-3xl">🎁</div>
          <div className="flex-1 space-y-1">
            <h4 className="text-sm text-white">이번 주 랭킹 보상</h4>
            <ul className="text-xs text-muted-foreground space-y-0.5">
              <li>🥇 1위: 스타벅스 기프티콘 3만원</li>
              <li>🥈 2~5위: 편의점 기프티콘 1만원</li>
              <li>🥉 6~20위: 랜덤박스 참여권</li>
            </ul>
          </div>
        </div>
      </Card>
    </div>
  );
}
