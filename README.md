# WebFramework
스프링 프레임워크에서 제공하는 기능을 최소화하고 일부 수정하여 웹 프레임워크 구현해보기

## 기본 설정
### 빈
 - 모든 빈은 싱글톤임
 - 모든 빈은 계층 관계가 없음
 - 모든 빈은 하나의 생성자만 갖을 수 있음
 - 의존성 주입은 생성자를 통해서만 가능
### ApplicationContext
 - ApplicationContext는 존재하지 않고, 빈을 관리만 하는 BeanContainer가 존재
 