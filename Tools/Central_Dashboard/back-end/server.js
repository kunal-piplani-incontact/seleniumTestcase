var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var sqlite3 = require('sqlite3').verbose();
var db = new sqlite3.Database('C:\\Users\\cagrawal\\Central_Dashboard\\back-end\\qualityDashBoard.db');

app.use(bodyParser.json());

app.use(function (req, res, next) {
	res.header("Access-Control-Allow-Origin", "*");
	res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
	next();
})

app.post('/api/message', function(req, res){
	console.log(req.body);
	res.status(200);
})

// db.serialize(function(req, res){
// 	var query = "SELECT * FROM Project p inner join TestingDetails td on p.projectId = td.projectId GROUP BY td.testingType, td.projectId order by td.executionDate desc";
//    	var query1 = "SELECT projectId, projectName, regOwner, registrationDate FROM Project";
//  	db.each(query, function(err, row) {
//   		if(err){
//   			console.log(err);
//   		}
//       console.log(row);
//       //console.log(row.projectId + " : " + row.projectName + " : " +row.regOwner + " : "+row.registrationDate);
//      // res.send(row);
//   });
// });

app.get('/api/message', function(req, res){
	var posts = [];
	var query1 = "SELECT * FROM Project p inner join TestingDetails td on p.projectId = td.projectId GROUP BY td.testingType, td.projectId order by td.projectName";
	
	db.serialize(function(){
	    db.each(query1, function(err, row) {
	        posts.push({Id: row.projectId, Owner:row.regOwner, Name: row.projectName, Type: row.testingType, Release:row.release, Date: row.executionDate, Total:row.total, Pass: row.pass, Fail:row.fail, Skip:row.skip, Duration:row.executionDuration})
	    }, function() {
	        // All done fetching records, render response
	         res.send(posts);
	    })
	})
});
	
/*app.get('/api/pieData', function(req, res){
	var posts = [];
	var query1 = "SELECT total, pass, fail, skip FROM Project p inner join TestingDetails td on p.projectId = td.projectId GROUP BY td.testingType, td.projectId order by td.projectName";
	console.log("at server function");
	db.serialize(function(){
	    db.each(query1, function(err, row) {
	        posts.push({ Total:row.total, Pass: row.pass, Fail:row.fail, Skip:row.skip})
	    }, function() {
	        // All done fetching records, render response
	         res.send(posts);
	    })
	})	
});*/

var server = app.listen(5000, function(){
	console.log("listening to port: ", server.address().port);
})
