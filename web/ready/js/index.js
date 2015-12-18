var address = "http://dev.balbum.net/";
// var address = "http://192.168.1.146:8080/";

var Start = {
    init: function() {
        $('.button-collapse').sideNav();
        $('.datepicker').pickadate({
            selectMonths: true, // Creates a dropdown to control month
            selectYears: 15 // Creates a dropdown of 15 years to control year
        });
        $('.scrollspy').scrollSpy();
        $('.timemachine-wrapper .row').pushpin({ top: $('.timemachine-wrapper').offset().top })
    }

}

var Upload = {
    init: function() {
        this.uploadPhoto();
    },

    /* 사진 프리뷰 */
    readURL: function(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#uploaded-photo').attr('src', e.target.result);
                $('#uploaded-photo').css('display', 'block').css('max-width', '100%');
            }
            reader.readAsDataURL(input.files[0]);
        }
    },
    uploadPhoto: function() {
        // 업로드버튼이나 올려진 사진을 누르면 hidden된 form을 누른다.
        $('.upload-photo-box, #uploaded-photo').click(function(event) {
            $("#upload-photo").click();
        });

        // form이 변경되면 fileread를 한다.
        $("#upload-photo").change(function(){
            Upload.readURL(this);
            $('.upload-photo-box').css('display', 'none');
        });
    }
}

var balbumApp = angular.module('balbumApp', []);

// var testData = [{
//         content: "테스트 콘텐츠 지롱",
//         modified: '2015-04-02',
//         imgUrl: "img/photo1.jpg",
//         babies: [
//             {name: "다정이",birth: "3개월", imgUrl: "img/baby1.jpeg"},
//             {name: "연우",birth: "2살", imgUrl: "img/baby2.jpeg"}],
//         cId: 1
//         }, {
//         content: "테스트를 또 하지롱 다정이",
//         modified: '2015-04-05',
//         imgUrl: "img/photo2.jpg",
//         babies: [
//             {name: "다정이",birth: "3개월", imgUrl: "img/baby1.jpeg"}],
//         cId: 2
//         }, {
//         content: "테스트를 또 하지롱 연우 ",
//         modified: '2015-04-07',
//         imgUrl: "img/photo3.jpg",
//         babies: [
//             {name: "연우",birth: "2살", imgUrl: "img/baby2.jpeg"}],
//         cId: 3
//         }
//     ];

// var addData = {
//         content: "",
//         modified: '2015-04-02',
//         imgUrl: "img/photo4.jpg",
//         babies: [
//             {name: "다정이",birth: "3개월", imgUrl: "img/baby1.jpeg"}]   ,
//         cId: 1
//         };

balbumApp.controller('CardController', function($scope, $http) {
    var cardTimeline = this;
    cardTimeline.cardList = [];
    var addData;

    $http.get(address + 'api/card').then( function(res) {
        console.log('card get is success');
        cardTimeline.cardList = res.data.cardList;
    }, function() {
        console.log('error');
    });
    cardTimeline.postCard = function() {
    }

    $('#ajaxForm').submit(function(e) {
        $(this).ajaxSubmit({
           //보내기전 validation check가 필요할경우
           beforeSubmit: function (data, frm, opt) {
                return true;
            },
            //submit이후의 처리
            success: function(responseText, statusText, xhr, $form){
                console.log("submit 성공했음");
                cardTimeline.cardList.unshift(responseText.res);
                console.log('이거다!', responseText.res);
                addData = responseText.res;
                console.log('카드리스트!', cardTimeline.cardList);
                $('#ajaxForm').clearForm();
                $scope.$apply();
            },
            //ajax error
            error: function(){
                alert("에러발생!!");
            }
        });
        e.preventDefault();
        return false;
    });


    // $scope.submitForm = function() {
    //     console.log('go to http!');
    //     $http({
    //         method  : 'POST',
    //         // method  : 'JSONP',
    //         url     : address + 'api/card',
    //         headers : {'Content-Type': 'multipart/form-data'},
    //         data    : $scope.card, //forms user object
    //         data: {
    //             email: "test1",
    //             token: "token",
    //             upload: $scope.file
    //         },
    //         transformRequest: function (data, headersGetter) {
    //             var formData = new FormData();
    //             angular.forEach(data, function (value, key) {
    //                 formData.append(key, value);
    //             });

    //             var headers = headersGetter();
    //             delete headers['Content-Type'];
    //             return formData;
    //         }
    //     })
    //     .success(function(data) {
    //         console.log(data);
    //         if (data.errors) {
    //             $scope.errorName = data.errors.name;

    //         } else {
    //             $scope.message = data.message;
    //             addData.content = $scope.card.content;
    //             testData.unshift(addData);
    //             $scope.card = '';
    //         }
    //     });
    // };
});

balbumApp.controller('postController', function($scope, $http) {
    $scope.card = {};

    // var data = {}; //file object


});

$(function(){
    Start.init();
    Upload.init();
});

