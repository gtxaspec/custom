#!/system/bin/sh

##INSTALL CUSTOM RECOVERY

p10_sha="b1042aee1d567d318c2d417337c74946cc40c2e2"
p10_install="a2a36f22d153929f0b5e39a3f358c5e8922dd15c"
stock_sha="d11c3b52dd8f3191e949e755fb5c53f203ab179a"
custom=0


install_check() {

if [ `getprop | grep ro.build.date.utc | sed 's/.*\[\([^]]*\)\].*/\1/g'` -ge "1513323821" ]; then

#check integrity of installation files
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
#install custom recovery
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
#verify installed recovery integrity
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
#failed both installs.

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

