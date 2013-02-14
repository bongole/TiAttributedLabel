function walk( d, r, pos ){
    var nodes = d.childNodes;
    var txt = '';
    for(var i = 0; i < nodes.length; i++ ){
        var n = nodes.item(i);
        if( n.nodeType === n.TEXT_NODE ){
            txt += n.nodeValue;
            pos += txt.length;
        }
        else{
            var subtxt = walk( n, r, pos );
            
            var attrs = n.attributes;
            var attro = {};
            for( var j = 0; j < attrs.length; j++){
                var nn = attrs.item(j);
                attro[nn.name] = nn.value;
            }
            
            var o = {};
            o[n.nodeName] = attro;
            
            r.push([pos, subtxt.length, o]);
            
            txt += subtxt;
            pos += subtxt.length;
        }
    }
    
    return txt;
}

exports.createALabel = function(opt){
    var l = this.createLabel(opt);
    
    // dummy listener for catching touch events
    l.addEventListener('click', function(){});
    
    l.setHTMLText = function(txt){
          var doc = Ti.XML.parseString('<xml>' + txt.replace(/<br\/?>/ig, "\n") + '</xml>');
          var result = [];
          var t = walk( doc, result, 0);
          
          Ti.API.info(t);
          Ti.API.info(JSON.stringify(result));
          
          l.attributedText = {
              text: t,
              attributes: result.reverse()
          };
    };
    
    return l;
}