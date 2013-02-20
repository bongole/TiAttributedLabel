function walk( d, r, pos ){
    var nodes = d.childNodes;
    var txt = '';
    for(var i = 0; i < nodes.length; i++ ){
        var n = nodes.item(i);
        if( n.nodeType === n.TEXT_NODE ){
            txt += n.nodeValue;
            pos += n.nodeValue.length;
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
            
            var subtxt_len = subtxt.length;
            
            r.forEach(function(rc){
                // inherit attributes.
                // ex. <font fontSize="30dp">aaa<font color="red">bbb</font></font>
                // "bbb" should 30dp size and red color font.
                var start = rc[0];
                var end = rc[0] + rc[1];
                if( pos <= start && end <= (pos + subtxt_len) ){
                    var oc = rc[2];
                    for( var node_name in oc ){
                        if( n.nodeName == node_name ){
                            var attroc = oc[node_name];
                            for( var prop in attro ){
                                if( !attroc.hasOwnProperty(prop) ){
                                    attroc[prop] = attro[prop];
                                }
                            }
                        }
                    }
                }
            });
            
            r.push([pos, pos + subtxt_len, o]);
            
            txt += subtxt;
            pos += subtxt.length;
        }
    }
    
    return txt;
}

exports.createALabel = function(opt){
    var l = this.createAttributedLabel(opt);
    
    l.setHTMLText = function(txt){
          var doc = Ti.XML.parseString('<xml>' + txt.replace(/<br\/?>/ig, "\n") + '</xml>');
          var result = [];
          var t = walk( doc, result, 0);
          
          l.attributedText = {
              text: t,
              attributes: result
          };
    };
    
    return l;
}