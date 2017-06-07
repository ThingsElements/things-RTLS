package com.example.hatio.things_rtls.assist;

import com.firebase.client.Firebase;

public class FirebaseSetValue {
	private Firebase myFirebaseRef;
	private String childName;
	
	public FirebaseSetValue(String name) {
		
		myFirebaseRef = new Firebase("https://bluetoothscan.firebaseio.com/");
		childName = name;
//		myFirebaseRef.child(childName).addValueEventListener(new ValueEventListener() {
//	           @Override
//	           public void onDataChange(DataSnapshot snapshot) {
//	        	   if(snapshot.getValue() != null) {
//		        	   String text = snapshot.getValue().toString();
//	        	   }
//	           }
//	           @Override public void onCancelled(FirebaseError error) { }
//	      });
	}
	
	public Firebase getFirebase() {
		return myFirebaseRef;
	}
	
//	public void setValue(double pitch, double roll, double yaw, String uuid) {
//		myFirebaseRef.child("260").child("locations").child(uuid).child("lastUpdateTime").setValue(System.currentTimeMillis());
//		myFirebaseRef.child("260").child("locations").child(uuid).child("updateInterval").setValue(50000);
//		
//		myFirebaseRef.child("260").child("locations").child(uuid).child("props").child("pitch").setValue(pitch);
//		myFirebaseRef.child("260").child("locations").child(uuid).child("props").child("roll").setValue(roll);
//		myFirebaseRef.child("260").child("locations").child(uuid).child("props").child("yaw").setValue(yaw);
//		
////		myFirebaseRef.child("260").child("locations").child("bbbbb").child("lastUpdateTime").setValue(System.currentTimeMillis());
////		
////		myFirebaseRef.child("260").child("locations").child("bbbbb").child("props").child("pitch").setValue(pitch);
//////		myFirebaseRef.child("260").child("locations").child("bbbbb").child("props").child("roll").setValue(roll);
//////		myFirebaseRef.child("260").child("locations").child("bbbbb").child("props").child("yaw").setValue(yaw);
//	}
	
	public void setPosition(double pitch, double roll, double yaw, String uuid) {
		myFirebaseRef.child("260").child("locations").child(uuid).child("lastUpdateTime").setValue(System.currentTimeMillis());

		myFirebaseRef.child("260").child("locations").child(uuid).child("updateInterval").setValue(50000);
		
		myFirebaseRef.child("260").child("locations").child(uuid).child("props").child("pitch").setValue(pitch);
		myFirebaseRef.child("260").child("locations").child(uuid).child("props").child("roll").setValue(roll);
		myFirebaseRef.child("260").child("locations").child(uuid).child("props").child("yaw").setValue(yaw);
	}
	
	public void setStandardDeviation(String address, double avg, double distance, double standardDeviation) {
		myFirebaseRef.child(address).child("average").setValue(avg);
		myFirebaseRef.child(address).child("distance").setValue(distance);
		myFirebaseRef.child(address).child("standardDeviation").setValue(standardDeviation);
	}
}
