#!/system/bin/sh

mount=`find /mnt -maxdepth 5 -name "5009_60_gtx.zip" -exec echo "{}" \; | grep -E '/mnt/usb_storage|/mnt/external_sd|/mnt/external_sdio' | sed -r 's_^(/[^/]*){1}/([^/]*)/.*$_\2_g'`

echo "Rebooting..."
rm -rf /cache/*
mkdir /cache/recovery
cp /data/data/com.gtx.jy.custom/5009_rec_signed.zip /cache/5009_60.zip
cp /data/data/com.gtx.jy.custom/openrecoveryscript_stock /cache/recovery/openrecoveryscript

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
##reboot now

reboot recovery
