import { Dialog, DialogContent, DialogHeader, DialogTitle } from "./ui/dialog";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "./ui/tabs";
import { Badge } from "./ui/badge";
import { ScrollArea } from "./ui/scroll-area";
import { Heart, MessageSquare, TrendingUp, Clock } from "lucide-react";
import { type Vote } from "./VotingCard";

interface MyVotesSheetProps {
  isOpen: boolean;
  onClose: () => void;
  participatedVotes: Vote[];
  createdVotes: Vote[];
  onVoteClick: (vote: Vote) => void;
}

export function MyVotesSheet({
  isOpen,
  onClose,
  participatedVotes,
  createdVotes,
  onVoteClick,
}: MyVotesSheetProps) {
  const renderVoteItem = (vote: Vote, isCreated = false) => {
    const userOption = vote.options.find(opt => opt.id === vote.userVoted);
    const percentage = userOption
      ? Math.round((userOption.votes / vote.totalVotes) * 100)
      : 0;

    return (
      <button
        key={vote.id}
        onClick={() => {
          onVoteClick(vote);
          onClose();
        }}
        className="w-full text-left bg-card hover:bg-card/80 border border-white/10 rounded-lg p-4 transition-all hover:scale-[1.02] group"
      >
        <div className="flex items-start justify-between gap-3 mb-2">
          <div className="flex-1">
            <div className="flex items-center gap-2 mb-1">
              <Badge className="bg-gradient-to-r from-lime-500 to-emerald-500 text-black border-0 text-xs">
                {vote.category}
              </Badge>
              {vote.isHot && (
                <Badge className="bg-gradient-to-r from-orange-500 to-pink-500 text-white border-0 text-xs">
                  🔥 HOT
                </Badge>
              )}
            </div>
            <h3 className="text-white mb-1 line-clamp-2">{vote.title}</h3>
            {vote.description && (
              <p className="text-xs text-muted-foreground line-clamp-1 mb-2">
                {vote.description}
              </p>
            )}
          </div>
        </div>

        <div className="flex items-center gap-4 text-xs text-muted-foreground">
          <div className="flex items-center gap-1">
            <TrendingUp className="w-3 h-3" />
            <span>{vote.totalVotes.toLocaleString()}표</span>
          </div>
          {!isCreated && userOption && (
            <div className="flex items-center gap-1 text-lime-500">
              <Heart className="w-3 h-3 fill-current" />
              <span>{userOption.text} • {percentage}%</span>
            </div>
          )}
          {vote.timeLeft && (
            <div className="flex items-center gap-1">
              <Clock className="w-3 h-3" />
              <span>{vote.timeLeft} 남음</span>
            </div>
          )}
        </div>

        {isCreated && (
          <div className="mt-2 pt-2 border-t border-white/10">
            <div className="flex items-center gap-1 text-xs text-orange-400">
              <MessageSquare className="w-3 h-3" />
              <span>내가 만든 투표</span>
            </div>
          </div>
        )}
      </button>
    );
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[80vh] bg-[#181818] border-white/10">
        <DialogHeader>
          <DialogTitle className="text-2xl text-white flex items-center gap-2">
            <Heart className="w-6 h-6 text-lime-500" />
            내 투표 활동
          </DialogTitle>
        </DialogHeader>

        <Tabs defaultValue="participated" className="w-full">
          <TabsList className="grid w-full grid-cols-2 bg-black/50 border border-white/10">
            <TabsTrigger 
              value="participated"
              className="data-[state=active]:bg-gradient-to-r data-[state=active]:from-lime-500 data-[state=active]:to-emerald-500 data-[state=active]:text-black"
            >
              <Heart className="w-4 h-4 mr-2" />
              참여한 투표 ({participatedVotes.length})
            </TabsTrigger>
            <TabsTrigger 
              value="created"
              className="data-[state=active]:bg-gradient-to-r data-[state=active]:from-orange-500 data-[state=active]:to-pink-500 data-[state=active]:text-white"
            >
              <MessageSquare className="w-4 h-4 mr-2" />
              만든 투표 ({createdVotes.length})
            </TabsTrigger>
          </TabsList>

          <TabsContent value="participated" className="mt-4">
            <ScrollArea className="h-[50vh] pr-4">
              {participatedVotes.length > 0 ? (
                <div className="space-y-3">
                  {participatedVotes.map((vote) => renderVoteItem(vote, false))}
                </div>
              ) : (
                <div className="text-center py-16">
                  <div className="text-6xl mb-4">🗳️</div>
                  <h3 className="text-white mb-2">아직 참여한 투표가 없어요</h3>
                  <p className="text-sm text-muted-foreground">
                    재미있는 투표에 참여해보세요!
                  </p>
                </div>
              )}
            </ScrollArea>
          </TabsContent>

          <TabsContent value="created" className="mt-4">
            <ScrollArea className="h-[50vh] pr-4">
              {createdVotes.length > 0 ? (
                <div className="space-y-3">
                  {createdVotes.map((vote) => renderVoteItem(vote, true))}
                </div>
              ) : (
                <div className="text-center py-16">
                  <div className="text-6xl mb-4">✨</div>
                  <h3 className="text-white mb-2">아직 만든 투표가 없어요</h3>
                  <p className="text-sm text-muted-foreground">
                    친구들과 공유할 투표를 만들어보세요!
                  </p>
                </div>
              )}
            </ScrollArea>
          </TabsContent>
        </Tabs>
      </DialogContent>
    </Dialog>
  );
}
