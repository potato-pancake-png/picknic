import { Button } from "./ui/button";
import { Vote } from "lucide-react";

interface LoginScreenProps {
  onEmailSignup: () => void;
  onSocialLogin: () => void;
}

export function LoginScreen({ onEmailSignup, onSocialLogin }: LoginScreenProps) {
  return (
    <div className="min-h-screen bg-background flex items-center justify-center p-4">
      <div className="w-full max-w-md space-y-8">
        {/* Logo Section */}
        <div className="text-center space-y-4">
          <div className="flex justify-center">
            <div className="w-24 h-24 rounded-3xl bg-gradient-to-br from-lime-500 to-emerald-500 flex items-center justify-center shadow-2xl shadow-lime-500/20">
              <Vote className="w-14 h-14 text-black" strokeWidth={2.5} />
            </div>
          </div>
          <div>
            <h1 className="text-5xl text-white mb-2">Picknic</h1>
            <p className="text-muted-foreground">
              친구들과 함께 Pick하는 즐거움
            </p>
          </div>
        </div>

        {/* Login Buttons */}
        <div className="space-y-3">
          {/* Kakao Login */}
          <Button
            onClick={onSocialLogin}
            className="w-full h-14 bg-[#FEE500] hover:bg-[#FDD835] text-black border-0 gap-3 transition-all hover:scale-[1.02]"
          >
            <div className="w-6 h-6 rounded-full bg-black flex items-center justify-center">
              <span className="text-[#FEE500] text-xs">K</span>
            </div>
            <span>카카오로 시작하기</span>
          </Button>

          {/* Google Login */}
          <Button
            onClick={onSocialLogin}
            className="w-full h-14 bg-white hover:bg-gray-100 text-black border-0 gap-3 transition-all hover:scale-[1.02]"
          >
            <svg className="w-6 h-6" viewBox="0 0 24 24">
              <path
                fill="#4285F4"
                d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
              />
              <path
                fill="#34A853"
                d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
              />
              <path
                fill="#FBBC05"
                d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
              />
              <path
                fill="#EA4335"
                d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
              />
            </svg>
            <span>Google로 시작하기</span>
          </Button>

          {/* Divider */}
          <div className="flex items-center gap-4 py-2">
            <div className="flex-1 h-px bg-white/10" />
            <span className="text-sm text-muted-foreground">또는</span>
            <div className="flex-1 h-px bg-white/10" />
          </div>

          {/* Sign Up */}
          <Button
            onClick={onEmailSignup}
            className="w-full h-14 bg-gradient-to-r from-lime-500 to-emerald-500 hover:from-lime-600 hover:to-emerald-600 text-black border-0 gap-2 transition-all hover:scale-[1.02]"
          >
            <span>이메일로 회원가입</span>
          </Button>
        </div>

        {/* Footer */}
        <div className="text-center space-y-2">
          <p className="text-sm text-muted-foreground">
            계속 진행하면{" "}
            <button className="text-lime-500 hover:underline">
              이용약관
            </button>{" "}
            및{" "}
            <button className="text-lime-500 hover:underline">
              개인정보처리방침
            </button>
            에 동의하는 것으로 간주됩니다.
          </p>
        </div>

        {/* Additional Info */}
        <div className="pt-8 space-y-3">
          <div className="grid grid-cols-3 gap-4 text-center">
            <div>
              <div className="text-2xl mb-1">🔥</div>
              <p className="text-xs text-muted-foreground">실시간 HOT 투표</p>
            </div>
            <div>
              <div className="text-2xl mb-1">🏫</div>
              <p className="text-xs text-muted-foreground">우리학교 전용</p>
            </div>
            <div>
              <div className="text-2xl mb-1">🎁</div>
              <p className="text-xs text-muted-foreground">포인트 보상</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
