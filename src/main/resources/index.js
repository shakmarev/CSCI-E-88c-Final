
// The location of Uluru
const uluru = { lat: -25.344, lng: 131.031 };

// Initialize and add the map
function initMap() {
    // The map, centered at Uluru
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 4,
        center: uluru,
    });
    // The marker, positioned at Uluru
    const marker = new google.maps.Marker({
        position: uluru,
        map: map,
    });
}

window.initMap = initMap;

window.addEventListener("load", function(event) {
    var button = document.getElementById("button")
    button.addEventListener("click", buttonOnClick)
});

function buttonOnClick(){
    var text = document.getElementById("text")
    DeleteKartItems()
}

function DeleteKartItems() {
    $.ajax({
        type: "POST",
        url: 'Default.aspx/DeleteItem',
        data: "",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (msg) {
            $("#divResult").html("success");
        },
        error: function (e) {
            $("#divResult").html("Something Wrong.");
        }
    });
}