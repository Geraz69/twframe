$(document).ready(function() {
  $.getJSON( `api/v1/videos` ).done(function(data) {
    var $videos = $('#popular')
    $.each(data, function(index, video) {
      var $image = $('<img>', { src: "http://img.youtube.com/vi/" + video + "/2.jpg" })
      $('<a>').attr('href', '/watch.html?v=' + video).attr('id', video)
        .addClass('video').append($image).appendTo($videos)
    })
  })
})

