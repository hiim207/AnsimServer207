window.onload = () => {
    var imgCheck = "N";
    var imgZone = document.querySelector('#imageZone');
    var fileEvent = document.querySelector('#imageFile');

    //imageZone 영역 클릭 시 파일이벤트 처리, 자바스크립트에선 이벤트 발생 시 이벤트 정보를 담고 있는 event(e)라는 객체를 자동 생성.
    imgZone.addEventListener('click',(e)=>{
        fileEvent.click(e);	//사용자가 만든 UI를 통해 <input type="file">을 클릭한느 것과 같은 효과를 내게 됨.
    });

    fileEvent.addEventListener('change',(e)=>{
        const files = e.target.files; //선택한 파일 정보가 e.target.files(배열)에 저장
        showImage(files);
    });

    const showImage = (files) => {
        imgZone.innerHTML = '';
        const imgFile = files[0];

        if(imgFile.type.indexOf("image") < 0){
            alert("이미지 파일만 올려 주세요");
            return false;
        };

        const reader = new FileReader(); 	//new 연산자를 통해서 FileReader 객체를 reader가 참조(상속)
        reader.onload = function(event) {	//reader.readAsDataURL(imgFile)로 이미지를 읽고 나서 해야 할 일
            imgZone.innerHTML = "<img src=" + event.target.result + " id='uploadImg' style='width:350px; height:auto;'>";
            //console.log(event.target.result);
        };

        reader.readAsDataURL(imgFile);	//실제로 이미지 파일을 읽는 부분
        imgCheck = "Y";
        //console.log(event.target.result);
    }

    btnRegister.addEventListener('click',async() => {
        //유효성 검사
        if(imgCheck == 'N'){
            alert("프로필 이미지를 등록하세요");
            return false;
        }
        if(user_id.value == '' || user_id.value == undefined || user_id.value == null){
            alert("아이디를 입력하세요");
            user_id.focus();
            return false;
        }
        const beforeUserid = user_id.value;
        const afterUserid = beforeUserid.replaceAll(/\-/ig,"").replace(/\ /ig,"").trim();
        user_id.value = afterUserid;

        if(user_nm.value == ''){
            alert("이름을 입력하세요");
            user_nm.focus();
            return false;
        }

        const Pass = password.value;
        const Pass1 = password1.value;

        if(Pass == ''){
            alert("암호를 입력하세요");
            password1.focus();
            return false;
        }
        else if(Pass1==''){
            alert("암호확인을 입력하세요!");
            password1.focus();
            return false;
        }
        else if(Pass != Pass1){
            alert("비밀번호와 비밀번호 확인 값이 다릅니다.");
            password1.focus();
            return false;
        }

        //자바스크립트의 정규식을 이용해서 암호 규칙을 확인
        //암호 작성 규칙 1. 8-20자리 내의 영문, 숫자, 특수문자를 조합

        let num = Pass.search(/[0-9]/g);	//0-9까지의 숫자가 들어 있는지 검색. 검색해서 값이 없으면 -1을 리턴
        let eng = Pass.search(/[a-z]/ig);	//g:global ???, i:알파벳 대소문자 구분 없이 검색하는 옵션
        //let spe = Pass.search(/[`~!@#$%^&*|\|\'\";:\/?]/ig);		//특수문자가 포함되어 있는지 검색
        if(Pass.length<8 || Pass.length>20){
            alert("비밀번호는 8-20자 사이로 입력 가능합니다.");
            password.focus();
            return false;
        } else if(Pass.search(/\s/) != -1){	//\s는 스페이스 //스페이스를 했다면
            alert("암호는 공백 없이 입력해 주세요");
            password.focus()
            return false;
        } else if(num<0 || eng<0){
            alert("암호는 영문, 숫자, 특수문자를 혼합하여 입력해 주세요");
            password.focus()
            return false;
        }
        const gender = document.querySelectorAll('input[name=gender]:checked');

        const mbti = document.querySelector('select[name=mbti] option:checked')



        if(age.value == ''){
            alert("나이를 입력해주세요.");
            age.focus();
            return false;
        }
        const ageCheck = age.value;

        // 숫자를 찾아 배열로 반환
        let ageNum = ageCheck.match(/[0-9]/g);

        if (!ageNum || ageNum.length < 1 || ageNum.length > 2) {
            alert("잘못 입력 하셨습니다.");
            age.focus();
            return false;
        } else if (ageNum.includes(' ')) {
            alert("잘못 입력 하셨습니다.");
            age.focus();
            return false;
        }


        if(gender.length == 0){
            alert("성별을 선택하세요");
            return false;
        }

        if(mbti.value == '00'){
            alert("MBTI를 선택하세요");
            return false;
        }

        if(tel_no.value == ''){
            alert("전화번호를 입력하세요");
            tel_no.focus();
            return false;
        }
        const beforeTelno = tel_no.value;
        //-를 삭제하기, 공백 삭제하기 trim문자열 좌우의 빈 공백을 없애주는 함수
        const afterTelno = beforeTelno.replaceAll(/\-/ig,"").replace(/\ /ig,"").trim();
        tel_no.value = afterTelno;


        const formData = new FormData(RegistryForm);

        await fetch('/member/signup', {
            method: 'POST',
            body: formData
        }).then((response)=> response.json())
            .then((data)=>{
                if(data.message === 'GOOD'){
                    alert(decodeURIComponent(data.user_nm) + '님, 회원 가입을 축하 드립니다.');
                    document.location.href='/guide/map';
                }
            }).catch((error)=>{
                console.log("error = "+error);
            })
    });
}

//------------------------취미 전체 선택-------------------------------
const selectAll = (checkElement) => {

    const checkboxes = document.getElementsByName('hobby');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = checkElement.checked;
    });
}

const idCheck = async () => {   //function async idCheck 이런 형태??

    const user_id = document.querySelector('#user_id');
    console.log("hi");
    //얘랑 통신을 할 것임
    await fetch('/member/idCheck',{
        method: "POST",
        body: user_id.value
    }).then((response)=> response.text())
        .then((data)=> {
            const idCheckNotice = document.querySelector('#idCheckNotice');
            //아이디가 있으면 1 없으면 0반환
            if(data == 0){
                idCheckNotice.innerHTML = "사용 가능한 아이디입니다.";
            }
            else{
                idCheckNotice.innerHTML = "이미 사용중인 아이디입니다.";
                //userid.value='';
                user_id.focus();
            }

        })

}