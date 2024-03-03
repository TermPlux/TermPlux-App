import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:xterm/core.dart';

//default values
class Default {
  static const String appName = 'TermPlux';
  //默认快捷指令
  static const commands = [{"name":"检查更新并升级", "command":"sudo apt update && sudo apt upgrade -y"},
    {"name":"查看系统信息", "command":"neofetch -L && neofetch --off"},
    {"name":"清屏", "command":"clear"},
    {"name":"查看IP", "command":"hostname -I # 如果显示无权限(Permission denied)，请在全局设置里开启getifaddrs桥接"},
    {"name":"中断任务", "command":"\x03"},
    {"name":"安装图形处理软件Krita", "command":"sudo apt update && sudo apt install -y krita krita-l10n"},
    {"name":"卸载Krita", "command":"sudo apt autoremove --purge -y krita krita-l10n"},
    {"name":"安装视频剪辑软件Kdenlive", "command":"sudo apt update && sudo apt install -y kdenlive"},
    {"name":"卸载Kdenlive", "command":"sudo apt autoremove --purge -y kdenlive"},
    {"name":"安装科学计算软件Octave", "command":"sudo apt update && sudo apt install -y octave"},
    {"name":"卸载Octave", "command":"sudo apt autoremove --purge -y octave"},
    {"name":"安装WPS", "command":r"""cat << 'EOF' | sh && sudo apt update && sudo apt install -y /tmp/wps.deb
url=$(curl -L https://linux.wps.cn/ | grep -oP 'href="\K[^"]*arm64\.deb' | head -n 1)
wget "${url}?k=$(eval echo -n '7f8faaaa468174dc1c9cd62e5f218a5b/$(echo -n ${url} | cut -d/ -f4-)0x7f53d55201314' | md5sum -t | cut -d' ' -f1)&t=0x7f53d55201314" --header="User-Agent: Mozilla/5.0 (X11; Linux aarch64; rv:109.0) Gecko/20100101 Firefox/115.0" --header="Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8" --header="Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2" --header="Accept-Encoding: gzip, deflate, br" --header="Connection: keep-alive" --header="Referer: https://www.wps.cn/" --header="Upgrade-Insecure-Requests: 1" --header="Sec-Fetch-Dest: document" --header="Sec-Fetch-Mode: navigate" --header="Sec-Fetch-Site: cross-site" --header="Sec-Fetch-User: ?1" -O /tmp/wps.deb
EOF
rm /tmp/wps.deb"""},
    {"name":"卸载WPS", "command":"sudo apt autoremove --purge -y wps-office"},
    {"name":"安装CAJViewer", "command":"wget https://download.cnki.net/net.cnki.cajviewer_1.3.20-1_arm64.deb -O /tmp/caj.deb && sudo apt update && sudo apt install -y /tmp/caj.deb && bash /home/tiny/.local/share/tiny/caj/postinst; rm /tmp/caj.deb"},
    {"name":"卸载CAJViewer", "command":"sudo apt autoremove --purge -y net.cnki.cajviewer && bash /home/tiny/.local/share/tiny/caj/postrm"},
    {"name":"安装亿图图示", "command":"wget https://www.edrawsoft.cn/2download/aarch64/edrawmax_12.6.1-1_arm64_binner.deb -O /tmp/edraw.deb && sudo apt update && sudo apt install -y /tmp/edraw.deb && bash /home/tiny/.local/share/tiny/edraw/postinst; rm /tmp/edraw.deb"},
    {"name":"卸载亿图图示", "command":"sudo apt autoremove --purge -y edrawmax libldap-2.4-2"},
    {"name":"安装QQ", "command":"""wget \$(curl -L https://cdn-go.cn/qq-web/im.qq.com_new/latest/rainbow/linuxQQDownload.js | grep -oP 'deb":"\\K[^"]*arm64\\.deb' | head -n 1) -O /tmp/qq.deb && sudo apt update && sudo apt install -y /tmp/qq.deb && sed -i 's#Exec=/opt/QQ/qq %U#Exec=/opt/QQ/qq --no-sandbox %U#g' /usr/share/applications/qq.desktop; rm /tmp/qq.deb"""},
    {"name":"卸载QQ", "command":"sudo apt autoremove --purge -y linuxqq"},
    {"name":"安装UOS微信", "command":"wget https://home-store-packages.uniontech.com/appstore/pool/appstore/c/com.tencent.weixin/com.tencent.weixin_2.1.10_arm64.deb -O /tmp/wechat.deb && sudo apt update && sudo apt install -y /tmp/wechat.deb /home/tiny/.local/share/tiny/wechat/deepin-elf-verify_all.deb /home/tiny/.local/share/tiny/wechat/libssl1.1_1.1.1n-0+deb10u6_arm64.deb && sed -i 's#/opt/apps/com.tencent.weixin/files/weixin/weixin#/opt/apps/com.tencent.weixin/files/weixin/weixin --no-sandbox#g' /opt/apps/com.tencent.weixin/files/weixin/weixin.sh && echo '该微信为UOS特供版，只有账号实名且在UOS系统上运行时可用。在使用前请前往全局设置开启UOS伪装。\n如果你使用微信只是为了传输文件，那么可以考虑使用支持SAF的文件管理器（如：质感文件），直接访问小小电脑所有文件。'; rm /tmp/wechat.deb"},
    {"name":"卸载UOS微信", "command":"sudo apt autoremove --purge -y com.tencent.weixin deepin-elf-verify"},
    {"name":"安装钉钉", "command":"""wget \$(curl -L https://g.alicdn.com/dingding/h5-home-download/0.2.4/js/index.js | grep -oP 'url:"\\K[^"]*arm64\\.deb' | head -n 1) -O /tmp/dingtalk.deb && sudo apt update && sudo apt install -y /tmp/dingtalk.deb && sed -i 's#\\./com.alibabainc.dingtalk#\\./com.alibabainc.dingtalk --no-sandbox#g' /opt/apps/com.alibabainc.dingtalk/files/Elevator.sh; rm /tmp/dingtalk.deb"""},
    {"name":"卸载钉钉", "command":"sudo apt autoremove --purge -y com.alibabainc.dingtalk"},
    {"name":"修复无法编译C语言程序", "command":"sudo apt update && sudo apt reinstall -y libc6-dev"},
    {"name":"修复系统语言到中文", "command":"sudo localedef -c -i zh_CN -f UTF-8 zh_CN.UTF-8"},
    {"name":"启用回收站", "command":"sudo apt update && sudo apt install -y gvfs && echo '安装完成, 重启软件即可使用回收站。'"},
    {"name":"拉流测试", "command":"ffplay rtsp://127.0.0.1:8554/stream &"},
    {"name":"关机", "command":"stopvnc\nexit\nexit"},
    {"name":"???", "command":"timeout 8 cmatrix"}
  ];

  //默认wine快捷指令
  static const wineCommands = [
    {"name": "wine配置", "command": "wine64 winecfg"},
    {"name": "我的电脑", "command": "wine64 explorer"},
    {"name": "记事本", "command": "wine64 notepad"},
    {"name": "扫雷", "command": "wine64 winemine"},
    {"name": "注册表", "command": "wine64 regedit"},
    {"name": "控制面板", "command": "wine64 control"},
    {"name": "文件管理器", "command": "wine64 winefile"},
    {"name": "任务管理器", "command": "wine64 taskmgr"},
    {"name": "ie浏览器", "command": "wine64 iexplore"},
    {"name": "强制关闭wine", "command": "wineserver -k"}
  ];

  //默认小键盘
  static const termCommands = [
    {"name": "BackSpace", "key": TerminalKey.backspace},
    {"name": "Enter", "key": TerminalKey.enter},
    {"name": "Esc", "key": TerminalKey.escape},
    {"name": "Tab", "key": TerminalKey.tab},
    {"name": "↑", "key": TerminalKey.arrowUp},
    {"name": "↓", "key": TerminalKey.arrowDown},
    {"name": "←", "key": TerminalKey.arrowLeft},
    {"name": "→", "key": TerminalKey.arrowRight},
    {"name": "Del", "key": TerminalKey.delete},
    {"name": "PgUp", "key": TerminalKey.pageUp},
    {"name": "PgDn", "key": TerminalKey.pageDown},
    {"name": "Home", "key": TerminalKey.home},
    {"name": "End", "key": TerminalKey.end},
    {"name": "F1", "key": TerminalKey.f1},
    {"name": "F2", "key": TerminalKey.f2},
    {"name": "F3", "key": TerminalKey.f3},
    {"name": "F4", "key": TerminalKey.f4},
    {"name": "F5", "key": TerminalKey.f5},
    {"name": "F6", "key": TerminalKey.f6},
    {"name": "F7", "key": TerminalKey.f7},
    {"name": "F8", "key": TerminalKey.f8},
    {"name": "F9", "key": TerminalKey.f9},
    {"name": "F10", "key": TerminalKey.f10},
    {"name": "F11", "key": TerminalKey.f11},
    {"name": "F12", "key": TerminalKey.f12},
  ];

  static const String boot =
      "\$DATA_DIR/bin/proot -H --change-id=1000:1000 --pwd=/home/tiny --rootfs=\$CONTAINER_DIR --mount=/system --mount=/apex --mount=/sys --kill-on-exit --mount=/storage:/storage --sysvipc -L --link2symlink --mount=/proc:/proc --mount=/dev:/dev --mount=\$CONTAINER_DIR/tmp:/dev/shm --mount=/dev/urandom:/dev/random --mount=/proc/self/fd:/dev/fd --mount=/proc/self/fd/0:/dev/stdin --mount=/proc/self/fd/1:/dev/stdout --mount=/proc/self/fd/2:/dev/stderr --mount=/dev/null:/dev/tty0 --mount=/dev/null:/proc/sys/kernel/cap_last_cap --mount=/storage/self/primary:/media/sd --mount=\$DATA_DIR/share:/home/tiny/公共 --mount=\$DATA_DIR/tiny:/home/tiny/.local/share/tiny --mount=/storage/self/primary/Fonts:/usr/share/fonts/wpsm --mount=/storage/self/primary/AppFiles/Fonts:/usr/share/fonts/yozom --mount=/system/fonts:/usr/share/fonts/androidm --mount=/storage/self/primary/Pictures:/home/tiny/图片 --mount=/storage/self/primary/Music:/home/tiny/音乐 --mount=/storage/self/primary/Movies:/home/tiny/视频 --mount=/storage/self/primary/Download:/home/tiny/下载 --mount=/storage/self/primary/DCIM:/home/tiny/照片 --mount=/storage/self/primary/Documents:/home/tiny/文档 --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/.tmoe-container.stat:/proc/stat --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/.tmoe-container.version:/proc/version --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/bus:/proc/bus --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/buddyinfo:/proc/buddyinfo --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/cgroups:/proc/cgroups --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/consoles:/proc/consoles --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/crypto:/proc/crypto --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/devices:/proc/devices --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/diskstats:/proc/diskstats --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/execdomains:/proc/execdomains --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/fb:/proc/fb --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/filesystems:/proc/filesystems --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/interrupts:/proc/interrupts --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/iomem:/proc/iomem --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/ioports:/proc/ioports --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/kallsyms:/proc/kallsyms --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/keys:/proc/keys --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/key-users:/proc/key-users --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/kpageflags:/proc/kpageflags --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/loadavg:/proc/loadavg --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/locks:/proc/locks --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/misc:/proc/misc --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/modules:/proc/modules --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/pagetypeinfo:/proc/pagetypeinfo --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/partitions:/proc/partitions --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/sched_debug:/proc/sched_debug --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/softirqs:/proc/softirqs --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/timer_list:/proc/timer_list --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/uptime:/proc/uptime --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/vmallocinfo:/proc/vmallocinfo --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/vmstat:/proc/vmstat --mount=\$CONTAINER_DIR/usr/local/etc/tmoe-linux/proot_proc/zoneinfo:/proc/zoneinfo \$EXTRA_MOUNT /usr/bin/env -i HOSTNAME=TINY HOME=/home/tiny USER=tiny TERM=xterm-256color SDL_IM_MODULE=fcitx XMODIFIERS=@im=fcitx QT_IM_MODULE=fcitx GTK_IM_MODULE=fcitx TMOE_CHROOT=false TMOE_PROOT=true TMPDIR=/tmp MOZ_FAKE_NO_SANDBOX=1 DISPLAY=:4 PULSE_SERVER=tcp:127.0.0.1:4718 LANG=zh_CN.UTF-8 SHELL=/bin/bash PATH=/usr/local/sbin:/usr/local/bin:/bin:/usr/bin:/sbin:/usr/sbin:/usr/games:/usr/local/games \$EXTRA_OPT /bin/bash -l";

  static final ButtonStyle commandButtonStyle = OutlinedButton.styleFrom(
    tapTargetSize: MaterialTapTargetSize.shrinkWrap,
    minimumSize: const Size(0, 0),
    padding: const EdgeInsets.fromLTRB(4, 2, 4, 2),
  );

  static final ButtonStyle controlButtonStyle = OutlinedButton.styleFrom(
    textStyle: const TextStyle(fontWeight: FontWeight.w400),
    side: const BorderSide(color: Color(0x1F000000)),
    tapTargetSize: MaterialTapTargetSize.shrinkWrap,
    minimumSize: const Size(0, 0),
    padding: const EdgeInsets.fromLTRB(8, 4, 8, 4),
  );

  static const MethodChannel avncChannel = MethodChannel("avnc");
}
