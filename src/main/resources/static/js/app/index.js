var main = {
    init: function(){
        var _this = this;

        $('#btn-send').on('click', function(){
            alert('인증 번호를 발송했습니다.');
            var phone = $("#phoneNumber").val();
            _this.send();
        });

    },
    send : function(){
        $.ajax({
            url: "/check/sendSMS?phone=" + phone,
            type : "GET",
            dataType: "json",
            contentType : 'application/json; charset=utf-8'
        })
        .done(function(){

        })
        .fail(function(error){
            alert(error);
        })
    }

};

main.init();