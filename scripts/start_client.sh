read user

if [[ $user = r ]]; then
	net_id = "rsurti2"
elif [[ $user = s ]]; then
	net_id = "ssaxen4"
fi

cd /home/$net_id/ECE428_mp2/src
rm ece428/mp1/*.class
javac -cp . ece428/mp1/*.java
java -cp . ece428/mp1/Main
