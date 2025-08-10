<img src="https://capsule-render.vercel.app/api?type=rect&color=ffb14d&height=150&section=header&text=GCP%20배포용" />

<div align="center">
    <img src="src/main/resources/static/image/logo.png" width="500px">
</div>

### ⚠️ 주의사항

> 프로그래머스에서 25.08.18 이후 AWS를 지원하지 않음에 따라, 개인 포트폴리오에서의 사용을 위해 GCP에 서버를 올리는 방법을 다룬 내용입니다.
> 
> 이 브랜치의 코드를 다운한다는 가정 하에, 준비과정(DB, 도메인 등)과 Docker 이미지 생성 후 서버 배포까지의 전반적인 내용을 담고 있습니다.
>
> ⚠️ GCP 신규 사용시 제공되는 무료 크레딧이 있기에 GCP로의 배포를 다루었습니다. 잘못된 생성 또는 크레딧 소진으로 인해 발생하는 비용에 대한 책임을 지지 않습니다.
> 
> ⚠️ GCP SecretManager 사용시 6개까지의 시크릿 키만 무료이며, 그 이상의 시크릿 키 등록시 작업 10,000개당 $0.03의 요금이 발생합니다. SecretManger로 인한 비용 발생은 본인부담입니다. 책임지지 않습니다. 
> 요금에 대한 자세한 내용은 [공식문서](https://cloud.google.com/secret-manager/pricing?hl=ko) 를 참고하세요.
> 
> ⚠️ 그 외, 배포시에 발생하는 그 어떠한 비용 발생으로 인한 문제도 책임지지않습니다.

# 1. GCP 준비

> GCP 신규 사용자 기준으로 작성되었습니다.

### 1-0) GCP 계정 등록
1. [Google Cloud](https://cloud.google.com) 에 접속하여 로그인 합니다.
2. 상단의 "**콘솔**"을 클릭합니다.
3. "**무료 체험**" 또는 "**무료로 시작하기**"를 클릭합니다.
4. 계정정보를 입력하고 완료합니다.

### 1-1) 프로젝트 생성

> 신규 가입시 자동생성되는 프로젝트를 사용해도 좋지만, 새 프로젝트를 생성하는 것을 권장합니다.

1. 좌측상단의 현재의 프로젝트를 클릭한 뒤 "새 프로젝트" 를 클릭합니다.
2. 프로젝트 이름을 지정하고 만들기를 클릭합니다.
3. 약간의 시간후, 현재 프로젝트를 다시 클릭하면 생성된 프로젝트가 보일 것 입니다.

### 1-2) VM 인스턴스 생성
1. 좌측 상단 탐색 메뉴 아이콘 -> Compute Engine -> VM 인스턴스를 누릅니다.
2. Compute Engine API 사용 버튼을 누릅니다.
3. 활성화에는 약간의 시간이 소요되며, 활성화 된 후 VM 인스턴스에 접근할 수 있습니다.
4. VM 인스턴스 페이지에서 "**인스턴스 만들기**" 를 클릭합니다.
5. 인스턴스를 구성합니다. 아래의 단계를 따른 후 "만들기"를 클릭하세요.
   - 머신구성 페이지의 하단에서, 머신유형을 e2-micro로 변경합니다. 그외에는 이름정도만 수정하고 넘어가면 됩니다.
   - OS 및 스토리지에서 운영체제를 변경해주어야 합니다. '변경' 버튼을 누르고, 공개 이미지의 섹션의 운영체제를 Ubuntu 로 바꾸십시오.
   - 데이터 보호는 넘어가도 좋습니다.
   - 네트워킹 페이지에서 HTTP 트래픽 허용 및 HTTPS 트래픽 허용을 체크합니다.
   - 그 외 설정은 모두 넘어가도 좋습니다.

### 1-3) IAM 권한 설정
1. 좌측 상단 탐색 메뉴 아이콘 -> IAM 및 관리자 에 들어갑니다.
2. VM 인스턴스가 문제없이 생성되었다면 {숫자}compute@developer... 형식의 이름을 가진 구성원이 추가되어있을 것 입니다.
3. 해당 구성원의 수정페이지를 들어갑니다.
4. 다른 역할 추가 -> 새로운 역할을 "보안 비밀 관리자 보안 비밀 접근자"로 설정합니다.

# 2. Supabase 준비
### 2-1) Organization 생성
1. [Supabase](https://supabase.com/)에 접속하여 계정을 생성합니다.
2. 로그인 후, 우측상단의 "**Dashboard**" 에 들어갑니다.
3. 기존의 Organization이 있다면 활용해도 좋고, 없다면 "**New oraganization**" 을 눌러 새로운 Organization을 생성합니다.
- Organization 생성단계에서 "Create a new project" 팝업이 나오면 2-2 단계의 3번부터 진행하세요.

### 2-2) DB 생성
1. 생성한 Organization 또는 기존의 Organization에 들어갑니다.
2. "**New project**" 버튼을 눌러 새로운 프로젝트를 생성합니다.
3. 정보를 입력합니다.
   - Project name: 아무이름이나 지정하셔도 됩니다.
   - Password: DB에 접근할때 사용되는 비밀번호\(`DATABASE_PASSWORD`)입니다. 이후에 사용되니 꼭 기억해두세요.
4. 프로젝트 이름과 비밀번호를 지정하였다면 생성완료합니다.

### 2-3) 필요 정보 메모
- `DATABASE_PASSWORD` : 앞서 언급된 비밀번호입니다.
- `DATABASE_URL`
  1. 이전 단계에서 생성한 프로젝트 페이지로 이동합니다.
  2. 상단의 `Connect` 버튼을 누릅니다.
  3. `Connection String -> Type`을 `JDBC`로 변경합니다.
  4. `Session pooler` 의 주소에서 postgres까지의 주소입니다.
- `DATABASE_USER` : 이전 단계에서 확인한 주소에서 `user=` 뒤의 값 입니다.

# 3. Redis 준비
### 3-1) DB 생성
1. [Redis](https://redis.io)에 접속합니다.
2. 회원가입을 완료합니다. 로그인을 완료하면 자동으로 DB페이지로 이동됩니다.
3. "**New database**"를 눌러 새로운 DB를 생성합니다. (또는 신규회원 로그인시 자동으로 이동될 것 입니다.)
4. Free 플랜을 선택하고, DB이름 지정후 "**Create database**"를 눌러 마무리

### 3-2) 필요 정보 메모
- `REDIS_HOST`
  1. 이전 단계에서 생성한 DB 목록을 클릭하여 상세페이지로 이동합니다.
  2. `Connect to database -> Connect -> Redis CLI` 에서 "@" 뒤부터 ".com" 까지의 값입니다.
- `REDIS_PASSWORD` : 동일 위치에서 `default`의 값이 비밀번호입니다.
- `REDIS_PORT` : `REDIS_HOST`에서 "redis-" 뒤의 값이 포트입니다.

# 4. SMTP 준비
 
> SMTP는 인증번호 발송에 사용됩니다.
> 
> 2단계 인증이 활성화되어있는 구글 계정으로 진행해야 합니다.

### 4-1) Gmail 설정
1. [Gmail](https://mail.google.com) 에 접속합니다.
2. 빠른설정(톱니바퀴 아이콘) -> 모든 설정 보기 -> 전달 및 POP/IMAP 메뉴 -> IMAP 액세스 -> 자동 삭제 사용 -> 변경사항 저장 버튼 클릭

### 4-2) 앱 비밀번호 발급
1. 프로필 -> Google 계정 관리 -> 보안 으로 이동
2. 검색창에 "**앱 비밀번호**" 검색후 클릭
3. 앱 이름 기입후 만들기 클릭
4. 생성된 앱 비밀번호\(`EMAIL_PASSWORD`)를 잘 메모해 두세요.

# 5. Gemini API 키 발급
### 5-1) API 키 발급받기
1. [Goolgle AI Studio](https://aistudio.google.com/)에 접속합니다.
2. "**Get API key**" 를 클릭합니다.
3. "**API 키 만들기**" 를 클릭합니다. 생성해둔 GCP 프로젝트에 연결하거나 새로운 GCP프로젝트를 생성하여 API키를 만듭니다.
4. 생성한 API 키\(`GEMINI_API_KEY`)를 잘 메모해 두세요.

# 6. 도메인 발급(내도메인.한국)

> ✅ CloudFlare를 사용하실 예정이라면 건너뛰어도 좋습니다.
> 
> ⚠️ 추가로, 도메인을 직접 발급받아 서버를 구성하는 경우 무료라는 장점이 있지만, 설정이 매우 복잡해지고, 무료 도메인의 경우 특정기간마다 갱신 또는 재발급을 받아야 하는 단점도 존재하니,
> 충분히 고민후 진행하시길 바랍니다.

### 6-1) 외부 IP 확인
1. [Google Cloud](https://cloud.google.com) 에 접속합니다.
2. 탐색메뉴 -> Compute Engine -> VM 인스턴스 로 들어갑니다.
3. 실행중인 인스턴스의 외부 IP를 메모해 두세요.

> ⚠️ 서브넷이 설정되지 않은 인스턴스의 경우 임시 외부 IP가 할당됩니다. 이는 인스턴스가 재시작될때마다 IP 주소가 변한다는 것을 의미합니다.
> 외부 IP가 변경되지 않는 것을 원하시는 경우, 네트워크 인터페이스를 직접 생성 및 할당하여야합니다.

### 6-2) 도메인 발급받기
1. [내도메인.한국](https://내도메인.한국)에 접속한 후 회원가입을 진행합니다.
2. "일반 도메인 검색" 에서 원하는 도메인을 검색합니다.
3. 도메인 검색 결과에서 사용할 도메인의 등록하기 버튼을 클릭합니다.
4. "고급설정(DNS)"의 "IP연결(A)"에 서버의 외부 IP를 입력합니다.
5. 완료후, 해당 도메인으로 http연결을 시도해봅니다. 만약 "hello world"가 출력된다면 성공적으로 연결 된 것입니다.

> ⚠️ 현재 단계에서 서버내에 https인증서가 발급되지 않았으므로, https연결은 실패할 가능성이 큽니다.
> 
> ⚠️ 내도메인.한국 에서 발급받은 도메인은 기본적으로 3개월동안 무료이며, 해당 기간 전에 갱신시 추가로 1년동안 무료로 사용할 수 있습니다.

# 7. 소셜 로그인 준비
### 7-1) 카카오 소셜 로그인
- 다음의 [블로그](https://blogan99.tistory.com/91)를 참고하여 카카오 로그인 키를 발급받습니다.
- 주의사항
   - Redirect URI 등록시 `https://{백엔드 도메인 주소}/oauth2/authorization/kakao` 로 하셔야 합니다.
   - 비즈앱 등록후 이메일까지 받을 수 있도록 해야 합니다.
   - 최종적으로 필요한 것은 **카카오 소셜 로그인 API키**`(KAKAO_ID)`입니다. 잘 메모해 두세요.

### 7-2) 구글 소셜 로그인
- 다음의 [블로그](https://blogan99.tistory.com/90?category=1148702)를 참고하여 구글 로그인 키를 발급받습니다.
- 주의사항
  - 승인된 리디렉션 URI는 `https://{백엔드 도메인 주소}/login/oauth2/code/google` 로 하셔야 합니다.
  - 데이터 액세스에서 이메일 주소까지 받을 수 있도록 해야 합니다.
  - 최종적으로 필요한 것은 **클라이언트 ID**(`GOOGLE_ID`) 와 **클라이언트 보안 비밀번호**(`GOOGLE_SECRET`)입니다. 잘 메모해 두세요.

# 8. Docker 준비
> 회원가입이 완료되었다는 가정하에 작성되었습니다.
### 8-1) 로컬 환경
1. [Docker Hub](https://hub.docker.com/welcome)에서 데스크탑용 프로그램을 다운 받으세요.
2. Docker 이미지 빌드 전, 실행하시면 됩니다.
### 8-2) 클라우드 환경
1. [Google Cloud](https://cloud.google.com) 에 접속하여 로그인 합니다.
2. `콘솔`에 들어갑니다. 이전에 생성해둔 프로젝트에 접속해있는지 확인해주세요.
3. `탐색메뉴 -> Compute Engine -> VM 인스턴스` 에서 생성한 인스턴으 목록의 `SSH`를 눌러 콘솔에 접속합니다.
4. 아래의 코드를 한 줄씩 차례대로 실행합니다.

    ```shell
    sudo apt-get upgrade
    sudo apt-get install ca-certificates curl
    sudo install -m 0755 -d /etc/apt/keyrings
    sudo curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
    sudo chmod a+r /etc/apt/keyrings/docker.asc
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian \
      $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    sudo apt-get update
    ```
5. 아래의 코드를 입력하여 도커 엔진을 설치합니다.
    ```shell
    sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
    ```
6. 도커 로그인을 진행합니다.
    ```shell
   # 아이디 입력후 비밀번호 또는 로그인 토큰 입력을 해주어야 합니다.
   # 소셜 로그인으로 인해 비밀번호가 없다면, docker hub 의 계정 설정에서 비밀번호를 생성하거나, 로그인 토큰을 생성하여 사용하세요. 
    sudo docker login -u {your_docker_id}
    ```
   
# 9. HTTPS 통신 준비
> ⚠️ Cloudflare를 사용하는 경우 진행하지 않으셔도 됩니다.
> 
> ✅ 이전 단계에서 이어집니다. 서버 콘솔에서 계속 진행하세요.

### 9-1) SSL 인증서 준비
> ✅ 본 단계에서는 Let's Encrypt를 활용해 무료 SSL 인증서를 받아옵니다.
> 
> ⚠️ Let's Encrypt는 90일 동안만 유효합니다. 서버를 지속하고 싶은 경우 인증서가 만료되기전 수동으로 재발급 받거나, Certbot을 통한 자동갱신을 구현하세요.
> 
> ⚠️ 만약 `too many certificates (50) already issued for "kro.kr" in the last...` 오류가 발생하는 경우, 오류 메세지에 포함된 시간 이후에 다시 시도해보세요. Let's Encrypt는 1주일 동안 하나의 루트 도메인에 대해 최대 50개의 인증서만 발급할 수 있도록 제한을 두고 있는데, kro.kr 도메인이 이미 50개가 생성되어서 그렇습니다.
1. Certbot 설치
    ```shell
    sudo apt update
    sudo  apt install certbot
    ```
2. 인증서 발급 (스탠드얼론 모드)
    ```shell
    sudo certbot certonly --standalone -d {backend_domain}
    ```

### 9-2) NGINX 설치
> ✅ 서버 콘솔에서 진행하시면 됩니다.

1. NGINX 설치
    ```shell
    sudo apt update
    sudo apt install nginx
    ```
2. NGINX HTTPS 리버스 프록시 설정
    - 파일 생성
    ```shell
    sudo nano /etc/nginx/sites-available/springboot-app
    ```
    - 파일 내용 입력
    ```shell
    server {
        listen 80;
        server_name {backend_domain};

        return 301 https://$host$request_uri;
    }

    server {
    listen 443 ssl;
    server_name {backend_domain};

        ssl_certificate /etc/letsencrypt/live/{backend_domain}/fullchain.pem;
        ssl_certificate_key /etc/letsencrypt/live/{backend_domain}/privkey.pem;

        location /api/v1/notify {
                proxy_pass http://localhost:8080;
                proxy_http_version 1.1;

                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;

                proxy_buffering off;
                proxy_read_timeout 86400;
        }

        location / {
                proxy_pass http://localhost:8080;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
        }
    }
    ```
3. 심볼릭 링크 설정 및 NGINX 재시작
    ```shell
    sudo ln -s /etc/nginx/sites-available/springboot-app /etc/nginx/sites-enabled/
    sudo nginx -t  # 설정 확인
    sudo systemctl reload nginx
    ```
   
# 10. Secret Manager 키 입력
### 10-1) Secret Manager 키 입력
1. `GCP의 탐색메뉴 -> 보안 -> Secret Manager` 로 이동합니다.
2. `보안 비밀 만들기` 를 통해 키-값 들을 추가합니다.
3. 추가해야하는 키-값 들을 다음과 같습니다.
```text
# PASSWORD 또는 ID, KEY를 제외한 내용은 application-prod.properties에 직접 넣어도 됩니다.
# 다만, 이는 최소한의 보안사항만 유지하는 것이므로, 보안에 유의하세요.
# JWT_SECRET 값은 우선적으로 application-prod.properties에 작성해두었습니다. 
  보안을 강화하고자 한다면 SecretManager에 추가하시는것을 권장합니다.

REDIS_HOST : redis 연결 주소
REDIS_PASSWORD : redis 연결 비밀번호
REDIS_PORT : redis 주소 내의 포트 번호
DATABASE_URL : supabase 연결 주소
DATABASE_USER : supabase 연결 주소 내의 유저 이름
DATABASE_PASSWORD : supabase 비밀번호
EMAIL_NAME : SMTP 설정에 사용된 이메일
EMAIL_PASSWORD : SMTP 설정시 발급받은 앱 비밀번호
KAKAO_ID : 카카오 소셜 로그인 API ID
KAKAO_REDIRECT_URI : 카카오 소셜 로그인 redirect uri
GOOGLE_ID : 구글 소셜 로그인 API ID
GOOGLE_SECRET : 구글 소셜 로그인 API 비밀번호
GOOGLE_REDIRECT_URI : 구글 소셜 로그인 redirect uri
GEMINI_API_KEY : gemini api key
```

# 11. 코드 수정
### 11-1) `src/main/resources/application-prod.properties` 수정
- 데이터 관련 설정
```text
:: spring.jpa.hibernate.ddl-auto = {value}
 - create : 서버를 구동할 때마다 DB의 테이블을 삭제하고 새로 생성합니다.
 - update : 서버를 구동할 때, 기존의 DB 정보와 다른 부분만 업데이트 합니다.
 - none : 서버를 구동해도 DB에 어떠한 변경도 시도하지 않습니다.
 * 첫 구동시 create로 해두어야 DB가 생성됩니다.
 
:: spring.sql.init.mode = {value}
 - always : 서버를 구동할 때마다 DB에 저장된 데이터를 삭제하고, data.sql에 있는 데이터를 새로 삽입합니다.
 - never : 서버를 구동해도 DB에 저장된 데이터에 어떠한 변경도 시도하지 않습니다.
```

- 도메인 관련 설정
```text
:: app.domain = {value}
 - 백엔드 서버 도메인 주소입니다. https 주소를 작성하세요.
:: front-server.prod-domain = {value}
 - 프론트 서버 도메인 주소입니다. https 주소를 작성하세요.
```

- 민감 정보 관련 설정 : 10-1 과정에서 언급한 값들은 `{sm://key}` 형식으로 작성되어있습니다. 해당 부분들 중 원하는 부분을 수정하면 됩니다.

# 12. Docker 이미지 빌드 및 서버 구동
### 12-1) docker 이미지 빌드
1. Docker Desktop을 실행합니다.
2. 터미널에서 프로젝트 폴더 위치로 이동합니다.
3. 아래의 명령어를 순차적으로 입력하여 이미지를 생성합니다.
    ```shell
    ./gradlew clean build -x test
   docker build -t {docker_id}/{image_name} .
   docker push {docker_id}/{image_name}:latest
    ```
4. gcp의 콘솔에서 다음의 명령어를 순차적으로 입력하여 서버를 구동합니다.
    ```shell
    sudo docker pull {docker_id}/{image_name}
    sudo docker run -d -p 8080:8080 {docker_id}/{image_name}
    ```
5. 브라우저에서 해당 도메인을 https 로 접속하여 서버가 정상적으로 구동되었는지 확인하세요. "hello world"페이지가 나온다면 정상적으로 구동된 것입니다.

# 기타. Docker 구동 상태 및 로그확인 방법
### 1. docker 실행상태 확인
- 기본적으로 단계 12의 과정을 통해 Docker를 실행하면 '백그라운드'에서 도커 컨테이너가 구동되게 됩니다. 따라서 로그가 실시간으로 보여지지 않습니다. 이는 사용자가 서버 콘솔에서 나가도 서버가 계속 구동되도록 하기 위함입니다. 서버가 구동되고 있는지 확인하려면 아래의 명령어를 통해 확인하세요.
```shell
# 정상적으로 실행되었다면 하나의 컨테이너 목록이 보일 것 입니다.
sudo docker ps
```

### 2. 로그 확인
- 만약 이전 단계에서 어떠한 목록도 보이지 않거나 서버가 동작하지 않는다면 다음의 명령어를 순차적으로 실행하여 로그를 확인하세요.
```shell
# 종료된 컨테이너까지 확인할 수 있는 명령어 입니다.
sudo docker ps -a
# 로그를 확인하는 방법입니다.
# 가장 최근에 구동된 컨테이너가 가장 위에 있습니다.
sudo docker logs {NAMES}
```

---
### 🤗 끝입니다. 고생하셨습니다.
> ✅ 배포 중 문제가 발생한다면 저에게 연락주세요. 최대한 빠르게 도움을 드리겠습니다.
> 
> ⚠️ 본 문서는 프로그래머스 데브코스 5기 6회차 파이널프로젝트 1팀 깃허브충돌위원회(GCC)의 팀원분들이 개인 포트폴리오에서의 사용을 위해 백엔드 서버를 구동하는데 참고하는 목적으로 작성되었습니다.
> GCC 팀원이 아닌 분들의 문의, 또는 도움 요청은 받지 않습니다.
> 
> :: GCC_백엔드 서버 관리자 - 정성원 -
> 
> :: Discord : `o_harang`
