import { useState } from "react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { School, Mail, CheckCircle2, ArrowRight } from "lucide-react";
import { toast } from "sonner";

interface SchoolVerificationProps {
  onComplete: (schoolName?: string) => void;
  onSkip: () => void;
}

export function SchoolVerification({ onComplete, onSkip }: SchoolVerificationProps) {
  const [step, setStep] = useState<"select" | "verify">("select");
  const [selectedSchool, setSelectedSchool] = useState("");
  const [email, setEmail] = useState("");
  const [verificationCode, setVerificationCode] = useState("");
  const [isCodeSent, setIsCodeSent] = useState(false);
  const [isVerifying, setIsVerifying] = useState(false);

  const schools = [
    "서울고등학교",
    "경기고등학교",
    "한영고등학교",
    "대원외국어고등학교",
    "중앙대학교",
    "연세대학교",
    "고려대학교",
    "서강대학교",
    "성균관대학교",
    "한양대학교",
    "이화여자대학교",
    "서울대학교",
  ];

  const handleSendCode = () => {
    if (!email.includes("@")) {
      toast.error("올바른 이메일 형식을 입력해주세요");
      return;
    }
    
    // 학교 이메일 도메인 체크 (실제로는 백엔드에서 처리)
    const domain = email.split("@")[1];
    if (!domain || domain.length < 3) {
      toast.error("학교 이메일을 입력해주세요");
      return;
    }

    setIsCodeSent(true);
    toast.success("인증 코드가 발송되었습니다!");
  };

  const handleVerify = () => {
    if (!verificationCode || verificationCode.length !== 6) {
      toast.error("6자리 인증 코드를 입력해주세요");
      return;
    }

    setIsVerifying(true);
    
    // 인증 시뮬레이션
    setTimeout(() => {
      setIsVerifying(false);
      toast.success(`${selectedSchool} 인증이 완료되었습니다! 🎉`);
      setTimeout(() => {
        onComplete(selectedSchool);
      }, 500);
    }, 1500);
  };

  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <div className="w-full max-w-md space-y-6">
        {/* Header */}
        <div className="text-center space-y-4">
          <div className="flex justify-center">
            <div className="w-20 h-20 rounded-2xl bg-gradient-to-br from-lime-500 to-emerald-500 flex items-center justify-center shadow-2xl shadow-lime-500/20">
              <School className="w-10 h-10 text-black" strokeWidth={2.5} />
            </div>
          </div>
          <div>
            <h1 className="text-3xl text-white mb-2">학교 인증</h1>
            <p className="text-muted-foreground">
              {step === "select" 
                ? "학교를 선택하고 인증을 진행해주세요"
                : `${selectedSchool} 이메일로 인증해주세요`}
            </p>
          </div>
        </div>

        {/* Content */}
        <div className="space-y-4">
          {step === "select" ? (
            <>
              {/* School Selection */}
              <div className="space-y-3">
                <Label htmlFor="school" className="text-white">학교 선택</Label>
                <select
                  id="school"
                  value={selectedSchool}
                  onChange={(e) => setSelectedSchool(e.target.value)}
                  className="w-full h-12 px-4 rounded-lg bg-white/10 border border-white/20 text-white focus:outline-none focus:border-lime-500 focus:ring-2 focus:ring-lime-500/20"
                >
                  <option value="" disabled>학교를 선택하세요</option>
                  {schools.map((school) => (
                    <option key={school} value={school} className="bg-zinc-900">
                      {school}
                    </option>
                  ))}
                </select>
              </div>

              {/* Benefits */}
              <div className="bg-white/5 border border-white/10 rounded-lg p-4 space-y-2">
                <p className="text-sm text-lime-500 flex items-center gap-2">
                  <CheckCircle2 className="w-4 h-4" />
                  학교 인증 혜택
                </p>
                <ul className="text-sm text-muted-foreground space-y-1 ml-6">
                  <li>• 학교별 리더보드 참여 가능</li>
                  <li>• 우리학교 필터로 투표 확인</li>
                  <li>• 인증 배지 획득</li>
                  <li>• 보너스 포인트 +50P</li>
                </ul>
              </div>

              {/* Action Buttons */}
              <div className="space-y-3 pt-2">
                <Button
                  onClick={() => {
                    if (!selectedSchool) {
                      toast.error("학교를 선택해주세요");
                      return;
                    }
                    setStep("verify");
                  }}
                  disabled={!selectedSchool}
                  className="w-full h-12 bg-gradient-to-r from-lime-500 to-emerald-500 hover:from-lime-600 hover:to-emerald-600 text-black border-0 gap-2"
                >
                  <span>다음</span>
                  <ArrowRight className="w-4 h-4" />
                </Button>

                <Button
                  onClick={onSkip}
                  variant="ghost"
                  className="w-full h-12 text-muted-foreground hover:text-white hover:bg-white/5"
                >
                  건너뛰기
                </Button>
              </div>
            </>
          ) : (
            <>
              {/* Email Verification */}
              <div className="space-y-3">
                <div className="space-y-2">
                  <Label htmlFor="email" className="text-white">학교 이메일</Label>
                  <div className="flex gap-2">
                    <Input
                      id="email"
                      type="email"
                      placeholder="example@school.ac.kr"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      disabled={isCodeSent}
                      className="flex-1 h-12 bg-white/10 border-white/20 text-white placeholder:text-muted-foreground"
                    />
                    <Button
                      onClick={handleSendCode}
                      disabled={isCodeSent}
                      className="h-12 px-6 bg-white/10 hover:bg-white/20 text-white border border-white/20"
                    >
                      {isCodeSent ? "발송됨" : "발송"}
                    </Button>
                  </div>
                  <p className="text-xs text-muted-foreground">
                    학교 이메일 주소로 인증 코드를 발송합니다
                  </p>
                </div>

                {isCodeSent && (
                  <div className="space-y-2 animate-in fade-in slide-in-from-top-2">
                    <Label htmlFor="code" className="text-white">인증 코드</Label>
                    <Input
                      id="code"
                      type="text"
                      placeholder="6자리 코드 입력"
                      value={verificationCode}
                      onChange={(e) => setVerificationCode(e.target.value.replace(/\D/g, "").slice(0, 6))}
                      className="h-12 bg-white/10 border-white/20 text-white placeholder:text-muted-foreground text-center text-xl tracking-widest"
                      maxLength={6}
                    />
                  </div>
                )}
              </div>

              {/* Action Buttons */}
              <div className="space-y-3 pt-2">
                <Button
                  onClick={handleVerify}
                  disabled={!isCodeSent || verificationCode.length !== 6 || isVerifying}
                  className="w-full h-12 bg-gradient-to-r from-lime-500 to-emerald-500 hover:from-lime-600 hover:to-emerald-600 text-black border-0 gap-2"
                >
                  {isVerifying ? (
                    <>
                      <div className="w-4 h-4 border-2 border-black/20 border-t-black rounded-full animate-spin" />
                      <span>인증 중...</span>
                    </>
                  ) : (
                    <>
                      <CheckCircle2 className="w-4 h-4" />
                      <span>인증 완료</span>
                    </>
                  )}
                </Button>

                <div className="flex gap-2">
                  <Button
                    onClick={() => setStep("select")}
                    variant="ghost"
                    className="flex-1 h-12 text-muted-foreground hover:text-white hover:bg-white/5"
                  >
                    이전
                  </Button>
                  <Button
                    onClick={onSkip}
                    variant="ghost"
                    className="flex-1 h-12 text-muted-foreground hover:text-white hover:bg-white/5"
                  >
                    건너뛰기
                  </Button>
                </div>
              </div>
            </>
          )}
        </div>

        {/* Info */}
        <div className="text-center pt-4">
          <p className="text-xs text-muted-foreground">
            나중에 마이페이지에서도 학교 인증이 가능합니다
          </p>
        </div>
      </div>
    </div>
  );
}
