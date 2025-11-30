import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "./ui/dialog";
import { Button } from "./ui/button";
import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import { Progress } from "./ui/progress";
import { Gift, Sparkles, Coffee, ShoppingBag, Gamepad } from "lucide-react";
import { toast } from "sonner";

interface Reward {
  id: number;
  name: string;
  icon: JSX.Element;
  points: number;
  type: string;
  color: string;
}

interface RewardModalProps {
  isOpen: boolean;
  onClose: () => void;
  userPoints: number;
}

export function RewardModal({
  isOpen,
  onClose,
  userPoints,
}: RewardModalProps) {
  const [spinning, setSpinning] = useState(false);

  const rewards = [
    {
      id: 1,
      name: "스타벅스 아메리카노",
      icon: <Coffee className="w-8 h-8" />,
      points: 500,
      type: "exchange",
      color: "from-[#1DB954] to-[#1aa34a]",
    },
    {
      id: 2,
      name: "편의점 1만원",
      icon: <ShoppingBag className="w-8 h-8" />,
      points: 1000,
      type: "exchange",
      color: "from-[#1ED760] to-[#1DB954]",
    },
    {
      id: 3,
      name: "게임 아이템",
      icon: <Gamepad className="w-8 h-8" />,
      points: 800,
      type: "exchange",
      color: "from-[#1aa34a] to-[#179443]",
    },
    {
      id: 4,
      name: "랜덤 룰렛 (1회)",
      icon: <Sparkles className="w-8 h-8" />,
      points: 200,
      type: "random",
      color: "from-[#1ED760] to-[#14833b]",
    },
  ];

  const luckyBoxPrizes = [
    "🎉 스타벅스 기프티콘",
    "🎁 편의점 3천원",
    "💫 포인트 +100",
    "🎊 럭키박스 무료권",
    "🌟 포인트 +500",
    "⭐ 다시 도전!",
  ];

  const handleExchange = (reward: Reward) => {
    if (userPoints >= reward.points) {
      toast.success(`${reward.name}를 교환했습니다!`);
      onClose();
    } else {
      toast.error("포인트가 부족합니다");
    }
  };

  const handleLuckyBox = () => {
    if (userPoints < 200) {
      toast.error("포인트가 부족합니다");
      return;
    }

    setSpinning(true);
    setTimeout(() => {
      const prize = luckyBoxPrizes[Math.floor(Math.random() * luckyBoxPrizes.length)];
      setSpinning(false);
      toast.success(`당첨! ${prize}`);
    }, 2000);
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[85vh] overflow-y-auto bg-[#181818] border-white/10 text-white">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-white">
            <Gift className="w-5 h-5 text-[#1DB954]" />
            보상 센터
          </DialogTitle>
        </DialogHeader>

        <div className="space-y-6">
          {/* User Points */}
          <Card className="p-4 bg-gradient-to-r from-[#1DB954] to-[#1aa34a] text-black border-0">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-black/70 text-sm">보유 포인트</p>
                <div className="text-3xl mt-1">{userPoints}P</div>
              </div>
              <div className="text-5xl">💰</div>
            </div>
          </Card>

          {/* Lucky Box */}
          <Card className="p-5 bg-[#1DB954]/10 border border-[#1DB954]/20">
            <div className="flex items-start gap-4 mb-4">
              <div className="text-4xl">🎁</div>
              <div className="flex-1">
                <h3 className="mb-1 text-white">럭키박스 뽑기</h3>
                <p className="text-sm text-muted-foreground">
                  200포인트로 랜덤 보상에 도전하세요!
                </p>
              </div>
            </div>

            {spinning ? (
              <div className="text-center py-8">
                <div className="inline-block animate-spin text-6xl mb-4">🎰</div>
                <p className="text-sm text-muted-foreground">당첨 결과를 확인하는 중...</p>
              </div>
            ) : (
              <>
                <div className="grid grid-cols-3 gap-2 mb-4">
                  {luckyBoxPrizes.slice(0, 6).map((prize, index) => (
                    <div
                      key={index}
                      className="bg-white/5 rounded-lg p-3 text-center border border-[#1DB954]/20"
                    >
                      <div className="text-2xl mb-1">{prize.split(" ")[0]}</div>
                      <p className="text-xs text-muted-foreground">
                        {prize.split(" ").slice(1).join(" ")}
                      </p>
                    </div>
                  ))}
                </div>
                <Button
                  onClick={handleLuckyBox}
                  className="w-full bg-[#1DB954] hover:bg-[#1ED760] text-black gap-2 border-0"
                  disabled={spinning}
                >
                  <Sparkles className="w-4 h-4" />
                  럭키박스 뽑기 (200P)
                </Button>
              </>
            )}
          </Card>

          {/* Exchange Rewards */}
          <div>
            <h3 className="mb-4 text-white">포인트 교환</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {rewards
                .filter((r) => r.type === "exchange")
                .map((reward) => (
                  <Card
                    key={reward.id}
                    className="p-4 bg-card hover:bg-[#1f1f1f] transition-all border-white/10"
                  >
                    <div className="flex items-start gap-3 mb-4">
                      <div
                        className={`w-14 h-14 rounded-xl bg-gradient-to-br ${reward.color} text-black flex items-center justify-center shrink-0`}
                      >
                        {reward.icon}
                      </div>
                      <div className="flex-1">
                        <h4 className="text-sm mb-1 text-white">{reward.name}</h4>
                        <Badge
                          variant="outline"
                          className="bg-[#1DB954]/10 text-[#1DB954] border-[#1DB954]/30"
                        >
                          {reward.points}P
                        </Badge>
                      </div>
                    </div>

                    {userPoints >= reward.points ? (
                      <Button
                        onClick={() => handleExchange(reward)}
                        className="w-full bg-[#1DB954] hover:bg-[#1ED760] text-black border-0"
                      >
                        교환하기
                      </Button>
                    ) : (
                      <div className="space-y-2">
                        <div className="flex items-center justify-between text-sm">
                          <span className="text-muted-foreground">
                            {reward.points - userPoints}P 부족
                          </span>
                          <span className="text-muted-foreground">
                            {Math.round(
                              (userPoints / reward.points) * 100
                            )}%
                          </span>
                        </div>
                        <Progress
                          value={(userPoints / reward.points) * 100}
                          className="h-2 bg-white/10"
                        />
                      </div>
                    )}
                  </Card>
                ))}
            </div>
          </div>

          {/* How to Earn */}
          <Card className="p-4 bg-[#1DB954]/10 border border-[#1DB954]/20">
            <h4 className="mb-3 flex items-center gap-2 text-white">
              💡 포인트 획득 방법
            </h4>
            <div className="space-y-2 text-sm">
              <div className="flex items-center justify-between">
                <span className="text-white">• 출석 체크</span>
                <Badge variant="secondary" className="bg-white/10 text-white border-0">+5P</Badge>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-white">• 투표 참여</span>
                <Badge variant="secondary" className="bg-white/10 text-white border-0">+1P</Badge>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-white">• 투표 생성</span>
                <Badge variant="secondary" className="bg-white/10 text-white border-0">+2P</Badge>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-white">• 정답 맞추기</span>
                <Badge variant="secondary" className="bg-white/10 text-white border-0">+10P</Badge>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-white">• 역배 정답</span>
                <Badge className="bg-[#1DB954] text-black border-0">
                  +50P
                </Badge>
              </div>
            </div>
          </Card>
        </div>
      </DialogContent>
    </Dialog>
  );
}
