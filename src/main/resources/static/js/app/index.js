var main = {
    init: function(){
        var _this = this;

        $('#btn-send').on('click', function(){
            alert('인증 번호를 발송했습니다.');
            var phoneNumber = $("#phoneNumber").val();
            _this.send(phoneNumber);
        });

        $('#btn-index').on('click', function(){ /* 친구 찾기 전에 질문했는 지 검사 */
            var init = $("#init").val();
            if(init == 0){
                alert("당신 MBTI 질문을 안했네요?");
                location.href="/mbti";
            }
            else if(init == 1){
                alert("당신 관심사 질문을 안했네요?");
                location.href="/question";
            }
            else
                location.href="/search";
        });

        $('#btn-follower').on('click', function(){
            var user_id = $('#user_id').val();
            _this.follower(user_id);
        });

        $('#btn-black').on('click', function(){
            var user_id = $('#user_id').val();
            _this.black(user_id);
        });

        $('#btn-blackCancel').on('click', function(){
            var user_id = $('#user_id').val();
            _this.blackCancel(user_id);
        });

        $('#btn-followerCancel').on('click', function(){
            var user_id = $('#user_id').val();
            _this.followerCancel(user_id);
        });

    },
    send : function(phoneNumber){
        $.ajax({
            url: "/join/sendSMS",
            type : "GET",
            cache : false,
            data : { phoneNumber : phoneNumber},
            success: function(data){
                $("#btn-send-check").click(function(){
                    var numStr = $("#numStr").val();
                    if(data == numStr){
                        alert("성공이용!");
                        window.location.href = "/joinID";
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
                location.reload();
            }
        })
    },
    followerCancel : function(user_id){
        $.ajax({
            url : "/friendDetail/followerCancel",
            type : "GET",
            cache : false,
            data : {user_id : user_id},
            success: function(data){
                alert("팔로워를 해제 했습니다.");
                location.reload();
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
                location.reload();
            }
        })
    },
    blackCancel : function(user_id){
        $.ajax({
            url : "/friendDetail/blackCancel",
            type : "GET",
            cache : false,
            data : {user_id : user_id},
            success: function(data){
                alert("차단을 해제 했습니다.");
                location.reload();
            }
        })
    }

};

main.init();
