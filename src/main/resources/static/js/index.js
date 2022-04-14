var main = {
    init: function(){
        var _this = this;
        $('#btn-send').on('click', function(){
            const phoneNumber = $('#phoneNumber').val();
            alert('인증 번호를 발송했습니다.')
            _this.send();
        });
    },
    send : function(){
        var data = {
            phoneNumber : $('#phoneNumber').val()
        };

        $a.jax({
            type : 'GET',
            url : '/check',
            dataType : 'json',
            contentType : 'application/json; charset=utf-8',
            data : JSON.stringify(data)
        }).done(function(data){
            const checkNum = data;
            alert('인증 번호: ' + checkNum);
            $('btn-send-check').click(function(){
                const numStr = $('#numStr').val();
                if(checkNum == numStr){
                    alert('인증이 성공했습니다.');
                    window.location.href = '/join';
                }
                else{
                    alert('인증이 실패했습니다..');
                }
            })
        }).fail(function(error){
            alert(JSON.stringify(error))
        });
    }
};

main.init();