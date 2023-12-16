window.onload = async() => {

    // 쿠키 가져 오기
    const getCookie = (name) => {
        const cookies = document.cookie.split(`; `).map((el)=>el.split('='));
        let getItem = [];
        // console.log("cookies = "+ cookies);

        for(let i=0; i<cookies.length; i++){
            if(cookies[i][0] === name){
                getItem.push(cookies[i][1]);
                break;
            }
        }
        if(getItem.length > 0){
            return decodeURIComponent(getItem[0]);
        }
    }
    let user_id = getCookie('user_id');
    let password = getCookie('password');
    let authkey = getCookie('authkey');

    // user_id 쿠키가 존재하면...
    if(user_id !== undefined){
        document.querySelector('#rememberUser_id').checked = true;
        document.querySelector('#user_id').value = user_id;
    } else {
        document.querySelector('#rememberUser_id').checked = false;
    }

    // password 쿠키가 존재하면..
    if(password !== undefined){
        document.querySelector('#rememberPassword').checked = true;
        // Base64로 인코딩 된 password를 디코딩
        const decrypt = CryptoJS.enc.Base64.parse(password);
        const hashData = decrypt.toString(CryptoJS.enc.Utf8);
        password = hashData;

        document.querySelector('#password').value = password;
    } else {
        document.querySelector('#rememberPassword').checked = false;
    }

    // 자동 로그인 처리
    if(authkey !== undefined){
        let formData = new FormData();
        formData.append("authkey", authkey);

        await fetch('/member/login?autologin=PASS',{
            method:'POST',
            body: formData
        }).then((response) => response.json())
            .then((data)=>{
                if(data.message === 'GOOD'){
                    document.location.href='/guide/map';
                }  else {
                    alert('시스템 장애로 자동 로그인이 실패 했습니다.');
                }
            }).catch((error)=>{
                console.log("error = " + error);
            })

    }

}

const login = () => {
    document.loginForm.action = '/member/login';
    document.loginForm.submit();
}

// 로그인 처리

const loginCheck = async() => {

    const user_id = document.querySelector('#user_id');
    const password = document.querySelector('#password');
    const rememberMe = document.querySelector('#rememberMe');
    // const authkey = '12345';

    if(user_id.value === '') {
        alert("아이디를 입력하세요.");
        user_id.focus();
        return false;
    }


    if(password.value === ''){
        alert("패스워드를 입력하세요.");
        password.focus();
        return false;
    }

    let formData = new FormData();
    formData.append("user_id", user_id.value);
    formData.append("password", password.value);

    await fetch('/member/loginCheck',{
        method: "POST",
        body: formData
    }).then((response) => response.json())
        .then((data) => {
            if(data.message === 'GOOD'){
                cookieManage(user_id.value,password.value);
                login();
            }else if(data.message === 'ID_NOT_FOUND'){
                msg.innerHTML = '존재하지 않는 아이디입니다.';
            }else if(data.message === 'PASSWORD_NOT_FOUND'){
                msg.innerHTML = '잘못된 패스워드입니다.';
            }else {
                alert("시스템 장애로 로그인이 실패 했습니다.");
            }
        }).catch((error)=> {
            console.log("error = " + error);
        })
}

//-----------아이디 체크 관리-----------------
const checkRememberUser_id = () => {
    //아이디 선택 시 자동로그인 해제
    //boolean값을 리턴??
    if(document.querySelector('#rememberUser_id').checked){
        document.querySelector('#rememberMe').checked = false;
    }
}
//-----------비밀번호 체크 관리-----------------
const checkRememberPassword = () => {
    //패스워드 선택 시 자동로그인 해제
    if(document.querySelector('#rememberPassword').checked){
        document.querySelector('#rememberMe').checked = false;
    }
}

//--------자동 로그인 체크 관리--------------
const checkRememberMe = () => {
    //자동로그인 선택 시, 아이디/패스워드 기억 선택 해제
    if(document.querySelector('#rememberMe').checked){
        document.querySelector('#rememberUser_id').checked = false;
        document.querySelector('#rememberPassword').checked = false;
    }
}

// 쿠키 관리
const cookieManage = (user_id, password, authkey) => {
    // user_id 체크 되었을 경우
    if(rememberUser_id.checked){
        document.cookie = 'user_id=' + user_id + '; path=/; expires=Sun, 31, Dec 2023 23:59:59 GMT';
    } else {
        document.cookie = 'user_id=' + user_id + '; path=/; max-age=0';
    }

    // password가 체크되었을 경우
    if(rememberPassword.checked){
        //Base64(양방향 복호화 알고리즘)로 패스워드 인코딩
        const key = CryptoJS.enc.Utf8.parse(password);
        const base64 = CryptoJS.enc.Base64.stringify(key);
        password = base64;

        document.cookie = 'password=' + password + '; path=/; expires=Sun, 31, Dec 2023 23:59:59 GMT';
    } else {
        document.cookie = 'password=' + password + '; path=/; max-age=0';
    }

    // 자동 저장이 체크되었을 경우
    if(rememberMe.checked){
        document.cookie = 'authkey=' + authkey + '; path=/; expires=Sun, 31, Dec 2023 23:59:59 GMT';
    } else {
        document.cookie = 'authkey=' + authkey + '; path=/; max-age=0';
    }
}


const press = () => {
    if(event.keyCode == 13) loginCheck();
}