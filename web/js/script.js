+function(){

  $(document).ready(function(){
    $('html,body').animate({scrollTop: 0},'fast');
    $(".introText").delay(1000).fadeTo(500,1);
    $("#introBtn").delay(1500).fadeTo(1000,1);
  })

$("#introBtn").click(function(){
  $("#intro").animate({
    right:"100vw",
    opacity: "0"
  },1000);
  $("#description").animate({
    right:"0vw",
    bottom: "0",
    opacity: "1"
  },1000);
  $("#descriptionBtn").delay(1500).fadeTo(500,1);
});

$("#descriptionBtn").click(function(){
  $("#description").animate({
    right:"100vw",
    opacity:"0"
  },1000);
  $("#demo").animate({
    right:"0vw",
    opacity:"1"
  },1000);
  $(".d1").delay(1500).fadeTo(500,1);
  $(".d2").delay(2000).fadeTo(500,1);
  $("#scr").fadeIn(500);
})

}(jQuery);
