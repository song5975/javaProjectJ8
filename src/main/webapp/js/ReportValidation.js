// EvaluationValidation.js

document.addEventListener('DOMContentLoaded', function () {
  const form = document.forms['myformReport']; // 여기서 'myformReport'은 폼의 이름입니다.

  	form.addEventListener('submit', function (event) {
    event.preventDefault(); // 기본 제출 동작을 막습니다.
    
    if (!form) {
        console.error('폼을 찾을 수 없습니다.');
        return;
    }

    // 필수 입력 필드들을 확인합니다.
    const reportTitle = form['reportTitle'].value;
    const reportContent = form['reportContent'].value;

    // 값이 비어있는지 확인하여 메시지를 출력합니다.
    if (!reportTitle || !reportContent) {
      alert('값을 모두 입력하세요.');
    } else {
      form.submit(); // 필수 항목이 모두 입력되었으면 폼을 제출합니다.
    }
  });
});
