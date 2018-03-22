const int ledPins[] = {4,3,2};
const int inputPins[] = {7,6,5};
int ledStates[] = {LOW, LOW, LOW};
unsigned long previousMillis = 0;
int moles[3] = {0,0,0};
unsigned long moleExpiry[3];
const long interval = 1500;

int whacStates[3] = {HIGH, HIGH, HIGH};

int score = 0;

void setup() {
  // set the digital pin as output:
  for(int i=0; i<3; i++) {
    pinMode(ledPins[i], OUTPUT);
    pinMode(inputPins[i], INPUT);
  }
  Serial.begin(9600);
}

void decideMoles(int moles[], int ledStates[], unsigned long moleExpiry[]) {
  for(int i=0; i<3; i++) {
      if (moles[i]==0) {
        if(random(7)<2) {
          moles[i] = 1;
          ledStates[i] = HIGH;
          moleExpiry[i] = millis() + 2000;
          if(digitalRead(inputPins[i])==HIGH) {
            whacStates[i] = LOW;
          }
          else {
            whacStates[i] = HIGH;
          }
        }
      }
   }
}

void clearMoles(int moles[], int ledStates[], unsigned long moleExpiry[]) {
  for(int i=0; i<3; i++) {
    if(moleExpiry[i]<millis()) {
      ledStates[i] = LOW;
      moles[i] = 0;
    }  
  }
}

int checkWhac(int moles[], int ledStates[], int whacStates[]) {
  int score = 0;
  for(int i=0; i<3; i++) {
    if(moles[i]) {
      if (digitalRead(inputPins[i])==whacStates[i]) {
        moles[i] = 0;
        ledStates[i] = 0;
        if(whacStates[i]==HIGH) {
          whacStates[i] = LOW;
        }
        else {
          whacStates[i] = HIGH;
        }
        score++;
      } 
    }
  }

  for(int i=0; i<3; i++)
    digitalWrite(ledPins[i], ledStates[i]);
  
  return score;
}

void loop() {

  score += checkWhac(moles, ledStates, whacStates);
//  Serial.print("Score:");
//  Serial.print(score);
//  Serial.print("\n");
  unsigned long currentMillis = millis(); 

  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    decideMoles(moles, ledStates, moleExpiry);
    clearMoles(moles, ledStates, moleExpiry);

//    for(int i=0; i<3; i++) {
////        if (digitalRead(inputPins[i])==HIGH) {
////          ledStates[i] = HIGH;
////        } 
////        else {
////          ledStates[i] = LOW;
////        }
//        if(moles[i]) {
//           ledStates[i] = HIGH;
//        }
//     }
//    

    for(int i=0; i<3; i++)
      digitalWrite(ledPins[i], ledStates[i]);

    Serial.print("---------------------------\n");
    for(int i=0; i<3; i++) {
      Serial.print(ledStates[i]);
    }
    Serial.print("\n");
    for(int i=0; i<3; i++) {
      Serial.print(moles[i]);
    }
    Serial.print("\n");
    Serial.print("Score:");
    Serial.print(score);
    Serial.print("\n");
  }
}
