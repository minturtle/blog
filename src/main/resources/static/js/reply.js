function createReply(){
    let reqBody = {
        content: $("#comment").val(),
        boardId: location.pathname.substring(7, location.pathname.length)
    }
    $.ajax({
        type:"POST",
        url : "/board/reply",
        data : JSON.stringify(reqBody),
        boardId : window.location.url,
        contentType:"application/json"
    }).done((res)=>{
        alert("댓글 작성이 완료되었습니다.")
        location.reload();
    })
}