var main = {
    init: function(){
        var _this = this;

        $('#btn-send').on('click', function(){
            alert('인증 번호를 발송했습니다.');
            var phoneNumber = $("#phoneNumber").val();
            _this.send(phoneNumber);
        });

        $('#moreInfo').on('click', function(){
            alert('무엇을 좋아하는 지 넣어주세요!');
        });

        $('#btn-index').on('click', function(){
            var username = $("#username").val();
            var init = $("#init").val();

            if(username != undefined){
                if(init == 0){
                    if(confirm("첫 로그인입니다!"))
                        document.location = "/question";
                }
            }
        });

        $('#btn-follower').on('click', function(){
            var user_id = $('#user_id').val();
            _this.follower(user_id);
        });

        $('#btn-black').on('click', function(){
            var user_id = $('#user_id').val();
            _this.black(user_id);
        });

        $('#btn-followerCencel').on('click', function(){
            var user_id = $('#user_id').val();
            _this.followerCencel(user_id);
        });

        $('#btn-blackCencel').on('click', function(){
                    var user_id = $('#user_id').val();
                    _this.blackCencel(user_id);
                });


    },
    send : function(phoneNumber){
        $.ajax({
            url: "/check/sendSMS",
            type : "GET",
            cache : false,
            data : { phoneNumber : phoneNumber},
            success: function(data){
                $("#btn-send-check").click(function(){
                    var numStr = $("#numStr").val();
                    if(data == numStr){
                        alert("성공이용!");
                        window.location.href = "/join";
                    }
                    else{
                        alert("아닌데용;");
                    }
                })
            }
        })
    },
    follower : function(user_id){
            $.ajax({
                url : "/friendDetail/follower",
                type : "GET",
                cache : false,
                data : {user_id : user_id},
                success: function(data){
                    alert("팔로우를 했습니다.");
                }
            })
    },
    black : function(user_id){
            $.ajax({
                url : "/friendDetail/black",
                type : "GET",
                cache : false,
                data : {user_id : user_id},
                success: function(data){
                    alert("차단을 했습니다.");
                }
            })
    },
    followerCencel : function(user_id){
                $.ajax({
                    url : "/friendDetail/followerCencel",
                    type : "GET",
                    cache : false,
                    data : {user_id : user_id},
                    success: function(data){
                        alert("팔로우를 취소 했습니다.");
                    }
                })
    },
    blackCencel : function(user_id){
                    $.ajax({
                        url : "/friendDetail/blackCencel",
                        type : "GET",
                        cache : false,
                        data : {user_id : user_id},
                        success: function(data){
                            alert("차단을 해제 했습니다.");
                        }
                    })
        }

};

main.init();
