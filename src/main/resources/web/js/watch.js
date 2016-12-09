var player, view;
var YTdeferred = $.Deferred();

window.onYouTubeIframeAPIReady = function() {
  YTdeferred.resolve(window.YT);
};

$(document).ready(function() {
  view = getView(window.location.href)
  if(view.video) {
    YTdeferred.done(function(YT) {
      loadVideo(video)
      loadComments(video)
    })
  }
})

function getView(url) {
  var queryString = url.slice(url.indexOf('?') + 1);
  return queryString.split('&').reduce(function (params, param) {
    var paramSplit = param.split('=').map(function (value) {
       return decodeURIComponent(value.replace('+', ' '));
    });
    params[paramSplit[0]] = paramSplit[1];
    return {
      comment: params['c'],
      video:   params['v'],
      time:    params['t'] || '0',
      limit:   params['l'] || '10',
    };
  }, {});
}

function getQueryString(url) {
   return
}

function loadVideo (video, time) {
  player = new YT.Player('player', { //see https://developers.google.com/youtube/player_parameters?playerVersion=HTML5
    videoId: video.id,
    height: '390',
    width: '640',
    playerVars: {
      start: video.time,
      autoplay: 0,
      modestbranding: 1,
      rel: 0,
      fs: 0
    }
  });
}

function loadComments(video) {
  $.getJSON( `api/v1/videos/${video.id}/comments?${getQueryString()}` ).done(function(data) {
    comments = data
    var $comments = $("#comments")
    while (comments.length > 0) {
      var comment = comments.shift()
      if (!comment) return;
      var response = $.ajax({
        url: "https://publish.twitter.com/oembed?url=https://twitter.com/anyuser/status/"+comment.id,
        dataType: "jsonp"
      });
      $.when(response, comment).done(function (response, comment){
        var $comment = $(response[0].html)
        var $quote = $comment.find('p').detach()
        var $author = $comment.find('a').detach().prepend($comment.text())
        $("<div>").attr("id", comment.id).data("meta", comment)
          .addClass("comment").append($quote, $author).appendTo($comments)
      })
    }
  })
}
