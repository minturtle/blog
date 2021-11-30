function join(){
        let reqBody = {
            username: $("#username").val(),
            email: $("#email").val(),
            password: $("#password").val()
        }
        $.ajax({
            type:"POST",
            url:"/user/join",
            data: JSON.stringify(reqBody),
            contentType : "application/json"
        }).done((res)=>{
            console.log(res);
            alert(res.message);
            location.href="/";
        }).fail((err)=>{
            let resBody = err.responseJSON;
            alert(resBody.err);
        })

        }

function login(){
    let reqBody = {
        username: $("#username").val(),
        password: $("#password").val()
    }
    $.ajax({
        type:"POST",
        url: "/user/login",
        contentType: "application/json",
        data: JSON.stringify(reqBody)
    }).done((res)=>{
        alert(res.message)
        location.href= "/"
    }).fail((err)=>{
        alert(err.responseJSON.err);
    })
}

function logout(){
    $.ajax({
        type:"POST",
        url : "/user/logout"
    }).done(()=>{
        location.href="/"
    })
}