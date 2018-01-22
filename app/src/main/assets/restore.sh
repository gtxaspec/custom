#!/system/bin/sh

p10_sha="b1042aee1d567d318c2d417337c74946cc40c2e2"
stock_sha="00dd0d581c089ee64c5b291d36d11f554f9ae5e3"
echo "Recovery Files Checksum Verification..."
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


