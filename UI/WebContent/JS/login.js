var url = "http://localhost:8081/api/account/createAdmin"

document.getElementById('toggleProfile').addEventListener('click', function () {
  [].map.call(document.querySelectorAll('.profile'), function(el) {
    el.classList.toggle('profile--open');
  });
});


var app = angular.module("ngApp", []);
app.controller("ngCtrl", function($scope,$http) {
    $scope.submit = function () {
        console.log("You clicked submit!");
        
        $http.post(url, $scope.account).success(function (response) {
			if (response != "") {
				console.log(response);
	 		}
			else{
				console.log("Else "+response);
			}
		});
        
        
    }
});