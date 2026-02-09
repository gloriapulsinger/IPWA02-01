/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
//wartet auf die Karte, da sonst das aktualisieren der Position nicht funktioniert
let gmapInitialized = false;

function waitForGMap() {
    if (gmapInitialized) return;

    const widget = PF('w_gmap');
    if (widget && widget.getMap()) {
        gmapInitialized = true;
        initCurrentPosition(widget.getMap());
    } else {
        setTimeout(waitForGMap, 100);
    }
}

function initCurrentPosition(map) {
    const circle = new google.maps.Circle({
        center: new google.maps.LatLng(41.381542, 2.122893),
        radius: 100,
        map: map,
        fillColor: '#FF0000',
        fillOpacity: 0.3,
        strokeColor: '#FF0000',
        strokeWeight: 9
    });

    if (navigator.geolocation) {
        navigator.geolocation.watchPosition(function (position) {
            const latLng = new google.maps.LatLng(
                position.coords.latitude,
                position.coords.longitude
            );
            map.setCenter(latLng);
            circle.setCenter(latLng);
            circle.setRadius(position.coords.accuracy);
        });
    }
}

waitForGMap();
        
//funktion zum scrollen
    function waitAndScroll(netzId) {
            const interval = setInterval(() => {
                const target = document.querySelector(
                    '.rows[data-netz-id="' + netzId + '"]'
                );

                if (target) {
                    clearInterval(interval);

                    const tr = target.closest("tr");
                    tr.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    tr.classList.add("highlight-row");

                    setTimeout(() => tr.classList.remove("highlight-row"), 2000);
                }
            }, 100);
            
        }
        
function toggleMarkers(category, visible) {
            let map = PF('w_gmap').getMap();
            map.markers.forEach(m => {
                if (m.icon.includes(category)){
                    m.setVisible(visible);
                }
            });
        }
        
function toggleAll(visible) {
            let map = PF('w_gmap').getMap();
            map.markers.forEach(m => {
                    m.setVisible(visible);
                
            });
        }
 
//aendert das Icon des markers
function updateMarkerStatus(netzID, status){
            let map = PF('w_gmap').getMap();
            map.markers.forEach(m => {
                    if(m.getTitle() === netzID){
                        if(status === 'GEBORGEN'){
                            m.setIcon("/ghostnetTracker/faces/jakarta.faces.resource/iconGreen.png?ln=images");
                        }else{
                            m.setIcon("/ghostnetTracker/faces/jakarta.faces.resource/iconYellow.png?ln=images");
                        }
                    }
                
            });
        }
        


