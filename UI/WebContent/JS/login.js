var checkAdminAccount = main_url+"account/checkAdminAccount";

document.getElementById('toggleProfile').addEventListener('click', function () {
  [].map.call(document.querySelectorAll('.profile'), function(el) {
    el.classList.toggle('profile--open');
  });
});


var app = angular.module("ngApp", []);
app.controller("ngCtrl", function($scope,$http,$window) {
    $scope.login = function () {
        console.log("You clicked submit!");
        
        $http.post(checkAdminAccount, angular.toJson($scope.account)).success(function (response) {
        	console.log("response");
        	
        	if (response == true) {
				$window.location.href = '/HTML/mainpage.html';
	 		}
			else{
				alert("Wrong username or password");
			}
		});
        
    }
});