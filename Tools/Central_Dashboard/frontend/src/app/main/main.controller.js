export class MainController {
  constructor ($http) {
    'ngInject';
    this.$http = $http;
    this.labels = ["PASS", "SKIPPED", "FAIL"];
    this.data = [];
    this.options = {
      legend:{
        display: true,
        position: 'right'
      }
    };

    this.getMessage();
  }

  getMessage(){
    var vm = this;
    this.$http.get('http://localhost:5000/api/message').then(function(result){
    vm.details = result.data;
    var pass = result.data[0].Pass;
    var skip = result.data[0].Skip
    var fail = result.data[0].Fail;
    console.log(pass, skip, fail);
    vm.data = [pass, skip, fail];
    vm.selectedRow = [result.data[0].Name, result.data[0].Type, result.data[0].Total, pass, skip, fail];
   });
  }

  getPiechartData(message){
    console.log(message.Name);     
     this.data = [message.Pass, message.Skip, message.Fail];
     this.selectedRow = [message.Name, message.Type, message.Total, message.Pass, message.Skip, message.Fail];
  }

  postMessage(){
    //console.log("post");
   this.$http.post('http://localhost:5000/api/message', {msg: this.message})
  }

  getData(){
   return  this.data;
  }
  getLabels(){
    return this.labels;
  }
  getOptions(){
    return this.options;
  }
  
}
