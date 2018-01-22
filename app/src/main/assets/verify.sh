#!/system/bin/sh

p10_install="a2a36f22d153929f0b5e39a3f358c5e8922dd15c"
stock_sha="00dd0d581c089ee64c5b291d36d11f554f9ae5e3"

dd if=/dev/block/mmcblk0p10 of=/cache/p10_verify.img


if [ `sha1sum /cache/p10_verify.img | cut -c1-40` == $stock_sha ]; then
echo "STOCK INSTALLED"
elif [ `sha1sum /cache/p10_verify.img | cut -c1-40` == $p10_install ]; then
echo "CUSTOM RECOVERY INSTALLED"
fi

rm -f /cache/p10*
