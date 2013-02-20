// This is a test harness for your module
// You should do something interesting in this harness 
// to test out the module and to provide instructions 
// to users on how to use it by example.


// open a single window
var win = Ti.UI.createWindow({
	backgroundColor:'white'
});

var TiAttributedLabelModule = require('com.bongole.ti.alabel');
l = TiAttributedLabelModule.createALabel({
    width: '200dp'
})

var html_text = "a"
+ '<font fontFamily="TestFont" fontSize="30dp" fontWeight="bonld" color="red">b</font>'
+ '<a href="http://www.example.com">c</a>'
+ '<font fontSize="30dp"><a href="http://www.example.com">d</a></font>'
+ 'e';

l.setHTMLText(html_text);

win.open();
