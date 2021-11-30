/*
function getBoards(page){
    $.ajax({
        url : "/board/boards?page=" + page,
        method : "GET",
        contentType : "application/json"
    }).done((res)=>{
        console.log(res)
    })
} */

function movePage(i){
    location.href="?page=" + i;
}