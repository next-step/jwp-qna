- [X] 모든 Entity에 @Id, @Entity 작성
  
- Answer
  - [X] Repository Test 작성
    - [X] save -> findById
    - [X] 삭제처리 변경으로 findByIdAndDeletedFalse 테스트 
    - [X] 삭제처리 변경으로 findByQuestionIdAndDeletedFalse 테스트 
  - [X] 객체 맵핑
- DeleteHistory
  - [X] Repository Test 작성
    - [X] save
  - [X] 객체 맵핑
- Question
  - [X] Repository Test 작성
    - [X] save -> findById
    - [X] 삭제처리 변경으로 findByIdAndDeletedFalse 테스트
    - [X] 삭제가 되어있으면, findByDeletedFalse에는 포함이 되면 안된다
  - [X] 객체 맵핑
- User
  - [ ] Repository Test 작성
    - [ ] save -> findById
    - [ ] UserId로 찾기
  - [ ] 객체 맵핑
- [ ] BaseEntity를 만들어 create, modified 처리하기