#!/system/bin/sh

p10_install="a2a36f22d153929f0b5e39a3f358c5e8922dd15c"
stock_sha="d11c3b52dd8f3191e949e755fb5c53f203ab179a"

dd if=/dev/block/mmcblk0p10 of=/cache/p10_verify.img


if [ `sha1sum /cache/p10_verify.img | cut -c1-40` == $stock_sha ]; then
echo "STOCK RECOVERY INSTALLED"
elif [ `sha1sum /cache/p10_verify.img | cut -c1-40` == $p10_install ]; then
echo "CUSTOM RECOVERY INSTALLED"
else
echo "SIGNATURE DOES NOT MATCH STOCK OR CUSTOM.  Please re-install your ROM."

fi

rm -f /cache/p10*

