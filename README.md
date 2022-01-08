# Do-Drone
## 호걸의 두드론 프로젝트!

### 호걸 유튜브 채널
+ https://www.youtube.com/channel/UCUP4IStWNEei6lNwVRUca2Q

### 호걸 notion 링크
+ https://www.notion.so/3fbcbdb4d4ee4c7d85809c8261e8d1b5

### Do-Drone proto
+ https://ovenapp.io/view/2VtwNkiRZEvzx2W57ZTWrSvuDpZIw5tP/Lw0jV

### 두드론 포스터세션 설명 링크
+ https://www.youtube.com/watch?v=qnByLQc75G8


# Do-Drone project

### 프로젝트 레포지토리 정리
+ https://github.com/ewhogirl/SSD_ObjectDetection : SSD MoblieNet V2 FPNLite 320x320을 이용한 손 검출 코드 및 메타데이터 추가 코드
+ https://github.com/RbChoi/Verification_OpenCV : mediapipe의 hand tracking을 통한 간단한 손 검출 코드
+ https://github.com/Juny-57/Unity : 키보드 조작을 통해 유니티에서 객체를 조종하는 코드


### 프로젝트의 목적

본 프로젝트는 모션 일치 방식의 드론 조종 프로그램이다.

손동작을 이용하여 드론을 조종하고 드론의 카메라뷰를 사용 중인 기기의 화면으로 동시에 확인할 수 있게 하여 사용성과 직관성을 높였다.
기존의 2D 조이스틱 기반이나 조종 애플리케이션과는 다른 색다른 조종 방식으로 사용자에게 다양한 조종 경험과 재미를 선사한다.

추가적으로 비행 시 장애물 탐지 기술을 이용하여 예상치 못한 경우에 드론 착륙 기능 등을 제공하여 안전성을 높이고자 한다. 

### 핵심 기술

1. 손
+ 사용 모델 : SSD MobileNet V2 FPNLite 320x320
+ 속도와 정확도 모두 평균 이상의 성능을 보이는 SSD 모델을 채택하여 손 인식 코드를 구현하였다.
+ 총 7개의 클래스의 이미지들을 LabelImg를 이용하여 레이블 작업을 하였다.
+ 학습 후 Tensorflow Lite로 변환하여 앱에 장착하였다.

2. 모바일
+ Android 태블릿용 Application

3. 드론
+ ESP32 보드를 장착한 아두이노 드론
+ INPUT 값에 따라 모터 속도를 변경하여 방향을 제어한다.
+ ESP32-CAM을 통해 비행 시 주변 환경을 촬영한다.
+ WiFi 라우터를 통해 ESP32, ESP32-CAM, 모바일 기기가 연결된다.
+ Latency를 최소화하기 위해 UDP 통신을 진행한다. 

<img width="40%" alt="motor" src="https://user-images.githubusercontent.com/60884739/142752759-472d0fbc-f122-4c44-8987-5cd044fcf6fc.png">

### 시스템 구조

<img width="60%" alt="그림1" src="https://user-images.githubusercontent.com/60884739/142752619-16b47ecd-9cdc-4cdb-8a55-f84fc5ee2cf0.png">


### 프로젝트 핵심 기술
1. 양질의 학습 사진 수집
+ 각도, 손의 위치, 배경, 명암 등 오버피팅을 방지하기 위한 자체 손 사진 데이터 9100장 확보

2. ESP32와 Client가 데이터를 주고 받기 위한 통신 설계
+ 속도를 위해 Bluetooth가 아닌 WiFi 통신
+ 빠른 데이터 전송을 위해 ESP32가 DB를 통하지 않고 Router를 통해 다른 기기와 통신

3. 드론 균형 잡기
+ 안정적인 비행을 위해서는 추가적인 HW 보완이 필요. 이를 위해 자이로 센서를 추가


--------------
<img width="10%" alt="logo" src="https://user-images.githubusercontent.com/60884739/120488805-12369100-c3f2-11eb-8a40-c97b0fd4d81e.png">
