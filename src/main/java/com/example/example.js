var rest = require('./node_modules/restler/lib/restler.js');

function uploadFile(ip, filepath, session){
    var prefix = "http://";
    var suffix = "/upload";
    var location = prefix + ip + suffix ;
    if(session != undefined){
        location = location +"?session=" + session;
    }

    rest.post( location, {
        multipart: true,
        data: {
            '[file]': rest.file(filepath)
        }
    }).on('complete', function(data) {
            console.log(data);
            return data;
        });
}

uploadFile("localhost:8090","1.jpg")