#include "twilio.hpp"
#include <WiFi.h> 
#include <HTTPClient.h>
#include <ArduinoJson.h> // NEW

#define BUTTON_PIN 6
#define BUZZER 4

int lastState = HIGH;
int currentState;
bool buzzerState = false;
bool callState= false;

static const char *ssid = "Galaxy";
static const char *password = "verysafepassword";

static const char *account_sid = "AC66845441dbe8fdb72c84e437d03e7cc8";
static const char *auth_token = "bb2c88e513030077215c5aff73773fa5";
static const char *from_number = "+18559842762";
static const char *to_number = "+18586665496";
static const char *message = "Emergency Location:";
static const char *google_maps_key = "AIzaSyBiFcSm33ybfJ8hyeEcKUhA4F7szyPMv2U";

Twilio *twilio;

String getWifiScanJson() {
  int n = WiFi.scanNetworks();
  String json = "[";
  for (int i = 0; i < n; i++) {
    if(i) json += ",";
    json += "{";
    json += "\"macAddress\": \"" + WiFi.BSSIDstr(i) + "\",";
    json += "\"signalStrength\": " + String(WiFi.RSSI(i));
    json += "}";
  }
  json += "]";
  return json;
}

String getGeoLocation(String wifiInfo) {
  HTTPClient http;
  http.begin("https://urldefense.com/v3/__https://www.googleapis.com/geolocation/v1/geolocate?key=__;!!Mih3wA!GfsMDLnm1T8vvxTazXWBUS3l61VZaFTrhMdI3e5hsFNgTd-4Dwg46WT2WDGeKoGikkd70DUgyGivIw$ " + String(google_maps_key));
  http.addHeader("Content-Type", "application/json");
  String httpRequestData = "{\"wifiAccessPoints\": " + wifiInfo + "}";
  int httpResponseCode = http.POST(httpRequestData);
  String response = "{}";
  if (httpResponseCode == 200) {
    response = http.getString();
    DynamicJsonDocument doc(1024);
    deserializeJson(doc, response);
    float lat = doc["location"]["lat"];
    float lng = doc["location"]["lng"];
    response = "https://urldefense.com/v3/__https://www.google.com/maps/?q=__;!!Mih3wA!GfsMDLnm1T8vvxTazXWBUS3l61VZaFTrhMdI3e5hsFNgTd-4Dwg46WT2WDGeKoGikkd70DXWZ41LCA$ " + String(lat, 6) + "," + String(lng, 6);
  }
  http.end();
  return response;
}

void setup() {
  pinMode(BUTTON_PIN, INPUT_PULLUP);
  pinMode(BUZZER, OUTPUT);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }

  twilio = new Twilio(account_sid, auth_token);
  delay(100);
}

void loop() {
  currentState = digitalRead(BUTTON_PIN);
  if (lastState == HIGH && currentState == LOW) {
    delay(100); 
    if(digitalRead(BUTTON_PIN) == LOW){
      buzzerState = !buzzerState;
      digitalWrite(BUZZER, buzzerState ? HIGH : LOW);  // turn on/off buzzer immediately
      
      if(buzzerState && callState==false){
        // start the message sending process
        String wifiInfo = getWifiScanJson();
        
        // Check button state and update buzzer again after WiFi scanning
        currentState = digitalRead(BUTTON_PIN);
        if (lastState == HIGH && currentState == LOW) {
          buzzerState = !buzzerState;
          digitalWrite(BUZZER, buzzerState ? HIGH : LOW);  // turn on/off buzzer immediately
        }
        lastState = currentState;

        String geoLocation = getGeoLocation(wifiInfo);
        String messageWithLocation = String(message) + " " + geoLocation + " Please help!";
 
        String response;
        bool success = twilio->send_message(to_number, from_number, messageWithLocation, response);
        callState=success;
        if (success) {
          Serial.println("Message sent successfully!");
          Serial.println("Response: " + response);
        } else {
          Serial.println("Message not sent");
        }
      } else if (!buzzerState) {
        // reset callState when buzzer is turned off
        callState = false;
      }
      while(digitalRead(BUTTON_PIN) == LOW);
    }
  }
  lastState = currentState;
  delay(100);
}
