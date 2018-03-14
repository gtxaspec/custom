#!/system/bin/sh

p10_sha="b1042aee1d567d318c2d417337c74946cc40c2e2"
p10_install="a2a36f22d153929f0b5e39a3f358c5e8922dd15c"
stock_sha="d11c3b52dd8f3191e949e755fb5c53f203ab179a"
custom=0

install_custom_recovery() {

install_check() {

if [ `getprop | grep ro.build.date.utc | sed 's/.*\[\([^]]*\)\].*/\1/g'` -ge "1513323821" ]; then

echo "Recovery Files Checksum Verification..."
if [ `sha1sum /data/data/com.gtx.jy.custom/p10_stock_rec.img | cut -c1-40` == $stock_sha ] && [ `sha1sum /data/data/com.gtx.jy.custom/recovery_new_keys.img | cut -c1-40` == $p10_sha ]; then
    echo "Checksums Match for Recovery Files..."
    install_custom
else
    echo "Checksums for copied files don't match.  Run program again."
fi

else
echo "You must be running at least AOSP build: \nAndroid/sofia3gr_car_64/sofia3gr_car:6.0.1/MMB29M/bsp12151541:userdebug/release-keys  \nInstallation will not proceed."
fi
}

install_custom(){

if [ $custom == "0" ]; then
    echo "Installing CUSTOM recovery..."
    dd if=/data/data/com.gtx.jy.custom/recovery_new_keys.img of=/dev/block/mmcblk0p10
    install_verify
    elif [ $custom == "1" ]; then
     echo "Installing CUSTOM recovery a second time"
      dd if=/data/data/com.gtx.jy.custom/recovery_new_keys.img of=/dev/block/mmcblk0p10
      install_verify
fi
}

install_verify(){

echo "Verifying Installation Integrity..."
dd if=/dev/block/mmcblk0p10 of=/cache/p10.img
if [ `sha1sum /cache/p10.img | cut -c1-40` == $p10_install ]; then
    echo "Custom Recovery installation complete!"
    echo "Completed at" `date`
    elif [ $custom == "0" ]; then
        echo "Signature Verification FAILED! Attempting installation again"
    custom=1
        install_custom
        elif [ $custom == "1" ]; then
         install_failed
fi
}


install_failed(){

echo "Signature Verification FAILED! Twice! Falling back to STOCK Recovery..."
dd if=/data/data/com.gtx.jy.custom/p10_stock_rec.img of=/dev/block/mmcblk0p10
dd if=/dev/block/mmcblk0p10 of=/cache/p10_2.img
if [ `sha1sum /cache/p10_2.img | cut -c1-40` == $stock_sha ]; then
echo "Signature Verification Passed for Stock Recovery!"
echo "Signature Verification FAILED! Restore Stock ROM from scratch."
echo "Something went wrong during installation.  We were unable to verify\n the signature of the custom recovery partition after flashing.\n  Please consult the forums."
fi

}

install_check


}

restore_stock_recovery() {

echo "Recovery Files Checksum Verification..."

restore() {

if [ `getprop | grep ro.build.date.utc | sed 's/.*\[\([^]]*\)\].*/\1/g'` -ge "1513323821" ]; then

if [ `sha1sum /data/data/com.gtx.jy.custom/p10_stock_rec.img | cut -c1-40` == $stock_sha ] && [ `sha1sum /data/data/com.gtx.jy.custom/recovery_new_keys.img | cut -c1-40` == $p10_sha ]; then
echo "Checksums Match for Stock Recovery Files..."
echo "Restoring STOCK recovery..."
dd if=/data/data/com.gtx.jy.custom/p10_stock_rec.img of=/dev/block/mmcblk0p10
echo "Installation Complete!"
else
echo "Checksums don't match.  Try again..."
fi

echo "Verifying Installation Integrity..."
dd if=/dev/block/mmcblk0p10 of=/cache/p10_3.img
if [ `sha1sum /cache/p10_3.img | cut -c1-40` == $stock_sha ]; then
echo "Signature Verification Passed for Stock Recovery!"
else
echo "Signature Verification FAILED! Restore Stock ROM from scratch."
echo "Something went wrong during installation.  We were unable to verify\n the signature of the recovery partition after flashing.  Please consult the forums."
dd if=/data/data/com.gtx.jy.custom/p10_stock_rec.img of=/dev/block/mmcblk0p10
fi
rm -f /cache/p10*

else
echo "You must be running at least AOSP build: \nAndroid/sofia3gr_car_64/sofia3gr_car:6.0.1/MMB29M/bsp12151541:userdebug/release-keys  \nInstallation will not proceed."
fi
}

restore

}

verify_custom_recovery() {
dd if=/dev/block/mmcblk0p10 of=/cache/p10_verify.img


if [ `sha1sum /cache/p10_verify.img | cut -c1-40` == $stock_sha ]; then
echo "STOCK RECOVERY INSTALLED"
elif [ `sha1sum /cache/p10_verify.img | cut -c1-40` == $p10_install ]; then
echo "CUSTOM RECOVERY INSTALLED"
else
echo "SIGNATURE DOES NOT MATCH STOCK OR CUSTOM.  Please re-install your ROM."

fi

rm -f /cache/p10*
}

reboot_custom_lbdroid() {
mount=`find /mnt -maxdepth 5 -name "5009_60_gtx.zip" -exec echo "{}" \; | grep -E '/mnt/usb_storage|/mnt/external_sd|/mnt/external_sdio' | sed -r 's_^(/[^/]*){1}/([^/]*)/.*$_\2_g'`

echo "Rebooting..."
rm -rf /cache/*
mkdir /cache/recovery
cp /data/data/com.gtx.jy.custom/5009_rec_signed.zip /cache/5009_60.zip
cp /data/data/com.gtx.jy.custom/openrecoveryscript_custom /cache/recovery/openrecoveryscript

if [ $mount == "usb_storage" ]; then
echo "usb_storage"
elif [ $mount == "external_sd" ]; then
echo "external_sd"
/sbin/busybox sed -i 's#usbotg#mnt/external_sd#g' /cache/recovery/openrecoveryscript
elif [ $mount == "external_sdio" ]; then
echo "external_sdio" 
/sbin/busybox sed -i 's#usbotg#mnt/external_sdio#g' /cache/recovery/openrecoveryscript
fi

touch /cache/recovery/command
echo "--update_package=/cache/5009_60.zip" > /cache/recovery/command
rm -f /cache/p10*

silence_amp

reboot recovery
}

reboot_custom_gtx() {
mount=`find /mnt -maxdepth 5 -name "5009_60_gtx.zip" -exec echo "{}" \; | grep -E '/mnt/usb_storage|/mnt/external_sd|/mnt/external_sdio' | sed -r 's_^(/[^/]*){1}/([^/]*)/.*$_\2_g'`

echo "Rebooting..."
rm -rf /cache/*
mkdir /cache/recovery
cp /data/data/com.gtx.jy.custom/5009_rec_signed.zip /cache/5009_60.zip
cp /data/data/com.gtx.jy.custom/openrecoveryscript_stock /cache/recovery/openrecoveryscript

if [ "$mount" == "usb_storage" ]; then
echo "usb_storage"
elif [ "$mount" == "external_sd" ]; then
echo "external_sd"
/sbin/busybox sed -i 's#usbotg#mnt/external_sd#g' /cache/recovery/openrecoveryscript
elif [ "$mount" == "external_sdio" ]; then
echo "external_sdio" 
/sbin/busybox sed -i 's#usbotg#mnt/external_sdio#g' /cache/recovery/openrecoveryscript
fi

touch /cache/recovery/command
echo "--update_package=/cache/5009_60.zip" > /cache/recovery/command
rm -f /cache/p10*

silence_amp

reboot recovery
}

reboot_to_twrp() {
echo "Rebooting..."
rm -rf /cache/*
mkdir /cache/recovery
cp /data/data/com.gtx.jy.custom/5009_rec_signed.zip /cache/5009_60.zip
touch /cache/recovery/command
echo "--update_package=/cache/5009_60.zip" > /cache/recovery/command
rm -f /cache/p10*

silence_amp

reboot recovery
}

touch_upgrade() {

rm -f /data/.gtx_upgrade
touch /data/.gtx_upgrade
echo "no_wipe_data" >> /data/.gtx_upgrade

}

silence_amp() {

i2cset -f -y 4 0x40 0x06 0xff
i2cset -f -y 4 0x40 0x20 0x00
echo 1 > /sys/fytver/muteAMP

}

recovery_bcb() {

printf '\x62\x6f\x6f\x74\x2d\x72\x65\x63\x6f\x76\x65\x72\x79\x00\x00\x00' | busybox dd of=/dev/block/mmcblk0p17 bs=1 seek=16384 count=16 conv=notrunc 
printf '\x72\x65\x63\x6f\x76\x65\x72\x79\x0a\x2d\x2d\x75\x70\x64\x61\x74' | busybox dd of=/dev/block/mmcblk0p17 bs=1 seek=16448 count=16 conv=notrunc 
printf '\x65\x5f\x70\x61\x63\x6b\x61\x67\x65\x3d\x2f\x63\x61\x63\x68\x65' | busybox dd of=/dev/block/mmcblk0p17 bs=1 seek=16464 count=16 conv=notrunc 
printf '\x2f\x35\x30\x30\x39\x5f\x36\x30\x2e\x7a\x69\x70\x0a\x00\x00\x00' | busybox dd of=/dev/block/mmcblk0p17 bs=1 seek=16480 count=16 conv=notrunc 

}

enable_performance_mode() {

mount -o rw,remount /system

mv /system/priv-app/ituxd/ituxd.apk /system/priv-app/ituxd/ituxd.apk.old

sed -i 's#echo interactive | tee /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor#echo performance | tee /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor#g' /system/bin/install-recovery.sh
sed -i '113s#echo 416000 | tee /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq;#echo 1200000 | tee /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq;#g' /system/bin/install-recovery.sh
sed -i '117s#echo 416000 | tee /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq;#echo 1040000 | tee /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq;#g' /system/bin/install-recovery.sh

}

if [ "$1" == "install_custom_recovery" ]; then
install_custom_recovery
elif [ "$1" == "restore_stock_recovery" ];then
restore_stock_recovery
elif [ "$1" == "verify_custom_recovery" ]; then
verify_custom_recovery
elif [ "$1" == "restart" ]; then
restart
elif [ "$1" == "reboot_custom_lbdroid" ]; then
reboot_custom_lbdroid
elif [ "$1" == "reboot_custom_gtx" ]; then
reboot_custom_gtx
elif [ "$1" == "reboot_to_twrp" ]; then
reboot_to_twrp
elif [ "$1" == "touch_upgrade" ]; then
touch_upgrade

else
echo "custom.sh"
echo $mount

fi
