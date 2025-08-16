# Score Counter

친구랑 보드게임하다가 불편해서 만들었습니다.
보드게임 등에서 여러 명의 플레이어 점수를 쉽게 관리할 수 있는 Android 앱입니다.

## 주요 기능

- 플레이어 추가/삭제/이름 변경
- 각 플레이어별 점수 증가/감소/초기화

## 빌드 방법

### 스크립트로 한 번에 빌드 (권장)

1. 최초 한 번 실행 권한 부여

```bash
chmod +x ./build_apk.sh
```

1. APK 빌드

```bash
./build_apk.sh
```

성공 시 생성 위치: `app/build/outputs/apk/debug/app-debug.apk`

ADB로 설치(선택):

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Android Studio에서 빌드

1. Android Studio에서 프로젝트를 엽니다.
2. Gradle Sync 후 실행하거나, Build > Build APK(s) 메뉴를 사용합니다.

## 폴더 구조

- `app/src/main/java/com/example/scorecounter/` : 주요 Kotlin 소스 코드
- `app/src/main/res/` : 리소스 파일(문자열, 테마 등)

---

이 앱은 Kotlin, MVVM, Jetpack Compose를 사용합니다.
