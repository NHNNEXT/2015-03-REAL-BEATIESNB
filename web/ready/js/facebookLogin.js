
window.fbAsyncInit = function() {
  FB.init({
    // appId      : '389047587948213', // 경륜
    appId      : '333726796797560', // 혜연언니
    cookie     : true,  // enable cookies to allow the server to access the session
    status     : true,
    xfbml      : true,  // parse social plugins on this page
    version    : 'v2.5' // use version 2.5
  });
    
    
  // FB.Event.subscribe('auth.authResponseChange', function(response) {
  //   if (response.status === 'connected') {
  //     console.log("페이스북 연결에 성공했습니다.");
  //     //SUCCESS
  //   }  else if (response.status === 'not_authorized') {
  //     console.log("페이스북 연결에 실패했습니다.");
  //     //FAILED
  //   } else {
  //     console.log("페이스북 로그아웃했습니다.");  
  //     //UNKNOWN ERROR
  //   }
  // }); 
  
};
    
/* 페북으로 로그인하기 버튼 눌렀을 때 */
function fbLogin(){
  FB.login(function(response) {
    if (response.authResponse) {

          FB.api('/me',{ locale: 'en_US', fields: 'name, email' }, function(response) { 
        
            fbEmailValidation(response.email, function(state){
              if(state == true){
                // 페북로그인 불가능 -> "아직 벨범에 가입되지 않은 페북아이디 입니다. 먼저 벨범에 가입해주세요!"
                $('#login-message').text('먼저 벨범에 가입해주세요!');
              } else {
                // 페북로그인 성공
                window.location.assign("/"); // mainPage로 감
              }
            });

          });
    } else {
      console.log('User cancelled login or did not fully authorize.');
    }
  },{scope: 'email,user_photos,user_videos'});
}


/* 페북으로 가입하기 버튼 눌렀을 때 */
function fbSignup(){
  FB.login(function(response) {
    if (response.authResponse) {

          FB.api('/me',{ locale: 'en_US', fields: 'name, email' }, function(response) { 

            // 받아오는 것들 
            // response.name, response.id, response.email

            var str ="";
            str +="<div class='row'><div class='input-field s12'><img id='upload-preview' class='circle responsive-img s12' alt='your image'/></div></div>";
            str +="<div class='row'><div class='input-field col s12'><input disabled value="+response.email+" id='disabled' type='text' class='validate><label for='disabled'></label></div></div>";
            str +="<div class='row'><div class='input-field  s12'><input id='signup-role' type='text' class='validate'><label for='role'>Role</label></div></div>";
            str +="<div class='row'><div id='signup-message'></div></div>";
            str +="<button name='submit' type='submit' value='submit' class='signup-btn modal-action modal-close waves-effect waves-light btn-large'>계정 만들기</button>";

            fbEmailValidation(response.email, function(state){
              if(state == false){
                // 페북가입 불가능 
                $('#signup-message').text('이미 가입된 페북계정입니다.');
              } else {
                // 페북가입 가능 
                getPhoto(); // 페북 프로필 사진 가져오기 
                $('#signup-form').html(str); // form창 변경 
              }
            });
          });
    } else {
      console.log('User cancelled login or did not fully authorize.');
    }
  },{scope: 'email,user_photos,user_videos'});
}



// 페북로그인/페북회원가입 버튼을 눌렀을 때, 페북이메일 주소가 이미 가입되어있는 이메일인지 확인하는 함수 
var fbEmailValidation = function(email, callback){
    // var elEmail = $('#signup-email');
    var url = "http://dev.balbum.net/";  
    var postString = "";       // post방식으로 처리하기 위한 파라미터들
    
    postString  = "email=" + email;
    $.ajax({                  
        type: "GET",
        url: url + "/api/user/isNewEmail",
        data: postString,
        success: function(res) {  //성공시 이 함수를 호출한다.
            callback(res.state);
       },
       error: function(res){
            console.log("[fbEmailValidation] ajax 실패라능");
       }
    });

}

// 페북 프로필 사진을 가져오는 함수 
function getPhoto() {
  FB.api('/me/picture?type=normal', function(response) {
    $("#upload-preview").attr("src", response.data.url);
  });
}


// Load the SDK asynchronously
(function(d){
  var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
  if (d.getElementById(id)) {return;}
  js = d.createElement('script'); js.id = id; js.async = true;
  js.src = "//connect.facebook.net/en_US/all.js";
  ref.parentNode.insertBefore(js, ref);
}(document));



