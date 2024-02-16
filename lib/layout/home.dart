import 'package:flutter/material.dart';
import 'package:termplux_app/layout/contor.dart';

import '../value/global.dart';
import '../utils/util.dart';
import '../workflow/workflow.dart';
import 'desktop.dart';
import 'loading.dart';
import 'terminal.dart';

// class MyHomePage extends StatefulWidget {
//   const MyHomePage({super.key, required this.title});
//
//   final String title;
//
//   @override
//   State<MyHomePage> createState() => _MyHomePageState();
// }
//
// class _MyHomePageState extends State<MyHomePage> {
//   //安装完成了吗？
//   //完成后从加载界面切换到主界面
//   bool isLoadingComplete = false;
//
//   @override
//   void initState() {
//     if (!isLoadingComplete) {
//       Workflow.workflow().then((value) {
//         setState(() {
//           isLoadingComplete = true;
//         });
//       });
//     }
//     super.initState();
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         title: Text(
//           isLoadingComplete
//               ? Util.getCurrentProp("name")
//               : widget.title,
//         ),
//       ),
//       body: isLoadingComplete ? Column(
//         mainAxisSize: MainAxisSize.min,
//         children: [
//           Expanded(
//             child: ValueListenableBuilder(
//               valueListenable: Global.screenIndex,
//               builder: (context, value, child) {
//                 return IndexedStack(
//                   index: Global.screenIndex.value,
//                   children: const [
//                     TerminalPage(),
//                     DesktopPage(),
//                     Contor()
//                   ],
//                 );
//               },
//             ),
//           ),
//         ],
//       ) : LoadingPage(),
//       bottomNavigationBar: ValueListenableBuilder(
//         valueListenable: Global.pageIndex,
//         builder: (context, value, child) {
//           return Visibility(
//             visible: isLoadingComplete,
//             child: ,
//           );
//         },
//       ),
//     );
//   }
// }

// compact阈值
const double compactWidthBreakpoint = 600;
// medium阈值
const double mediumWidthBreakpoint = 840;
// expanded阈值不就是比这俩都大呗
// 大小切换动画速率
const double transitionLength = 500;

enum SizeSelected { compact, medium, expanded }

class Home extends StatefulWidget {
  const Home({super.key, required this.title});

  final String title;

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> with SingleTickerProviderStateMixin {
  final GlobalKey<ScaffoldState> scaffoldKey = GlobalKey<ScaffoldState>();
  late final AnimationController controller;
  late final CurvedAnimation railAnimation;
  bool controllerInitialized = false;

  SizeSelected windowSize = SizeSelected.compact;

  //安装完成了吗？
  //完成后从加载界面切换到主界面
  bool isLoadingComplete = false;

  @override
  initState() {
    if (!isLoadingComplete) {
      Workflow.workflow().then((value) {
        setState(() {
          isLoadingComplete = true;
        });
      });
    }
    super.initState();
    controller = AnimationController(
      duration: Duration(milliseconds: transitionLength.toInt() * 2),
      value: 0,
      vsync: this,
    );
    railAnimation = CurvedAnimation(
      parent: controller,
      curve: const Interval(0.5, 1.0),
    );
  }

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    final double width = MediaQuery.of(context).size.width;
    final AnimationStatus status = controller.status;
    switch (width) {
      case < compactWidthBreakpoint:
        windowSize = SizeSelected.compact;
        if (status != AnimationStatus.reverse &&
            status != AnimationStatus.dismissed) {
          controller.reverse();
        }
      case < mediumWidthBreakpoint:
        windowSize = SizeSelected.medium;
        if (status != AnimationStatus.forward &&
            status != AnimationStatus.completed) {
          controller.forward();
        }
      default:
        windowSize = SizeSelected.expanded;
        if (status != AnimationStatus.forward &&
            status != AnimationStatus.completed) {
          controller.forward();
        }
    }
    if (!controllerInitialized) {
      controllerInitialized = true;
      controller.value = width > mediumWidthBreakpoint ? 1 : 0;
    }
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: controller,
      builder: (context, child) {
        return NavigationTransition(
          scaffoldKey: scaffoldKey,
          animationController: controller,
          railAnimation: railAnimation,
          appBar: AppBar(
            title: Text(
              isLoadingComplete ? Util.getCurrentProp("name") : widget.title,
            ),
          ),
          body: isLoadingComplete
              ? Expanded(
                  child: ValueListenableBuilder(
                    valueListenable: Global.screenIndex,
                    builder: (context, value, child) {
                      return IndexedStack(
                        index: Global.screenIndex.value,
                        children: const [
                          TerminalPage(),
                          DesktopPage(),
                          Contor()
                        ],
                      );
                    },
                  ),
                )
              : const LoadingPage(),
          navigationRail: ValueListenableBuilder(
            valueListenable: Global.screenIndex,
            builder: (context, value, child) {
              return Visibility(
                visible: isLoadingComplete,
                child: NavigationRail(
                  extended: windowSize == SizeSelected.expanded,
                  destinations: navRailDestinations,
                  selectedIndex: Global.screenIndex.value,
                  onDestinationSelected: (index) {
                    Global.screenIndex.value = index;
                  },
                  // trailing: ,
                ),
              );
            },
          ),
          navigationBar: ValueListenableBuilder(
            valueListenable: Global.screenIndex,
            builder: (context, value, child) {
              return Visibility(
                visible: isLoadingComplete,
                child: NavigationBar(
                  selectedIndex: Global.screenIndex.value,
                  onDestinationSelected: (index) {
                    Global.screenIndex.value = index;
                  },
                  destinations: appBarDestinations,
                ),
              );
            },
          ),
        );
      },
    );
  }
}

const List<NavigationDestination> appBarDestinations = [
  NavigationDestination(
    icon: Icon(Icons.terminal_outlined),
    selectedIcon: Icon(Icons.terminal),
    label: "终端",
    tooltip: "终端模拟器",
    enabled: true,
  ),
  NavigationDestination(
    icon: Icon(Icons.desktop_windows_outlined),
    selectedIcon: Icon(Icons.desktop_windows),
    label: "桌面",
    tooltip: '桌面',
    enabled: true,
  ),
  NavigationDestination(
    icon: Icon(Icons.video_settings_outlined),
    selectedIcon: Icon(Icons.video_settings),
    label: "控制",
    tooltip: '控制',
    enabled: true,
  )
];

class NavigationTransition extends StatefulWidget {
  const NavigationTransition(
      {super.key,
      required this.scaffoldKey,
      required this.animationController,
      required this.railAnimation,
      required this.navigationRail,
      required this.navigationBar,
      required this.appBar,
      required this.body});

  final GlobalKey<ScaffoldState> scaffoldKey;
  final AnimationController animationController;
  final CurvedAnimation railAnimation;
  final Widget navigationRail;
  final Widget navigationBar;
  final PreferredSizeWidget appBar;
  final Widget body;

  @override
  State<NavigationTransition> createState() => _NavigationTransitionState();
}

class _NavigationTransitionState extends State<NavigationTransition> {
  late final AnimationController controller;
  late final CurvedAnimation railAnimation;
  late final ReverseAnimation barAnimation;
  bool controllerInitialized = false;
  bool showDivider = false;

  @override
  void initState() {
    super.initState();

    controller = widget.animationController;
    railAnimation = widget.railAnimation;

    barAnimation = ReverseAnimation(
      CurvedAnimation(
        parent: controller,
        curve: const Interval(0.0, 0.5),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final ColorScheme colorScheme = Theme.of(context).colorScheme;
    return Scaffold(
      key: widget.scaffoldKey,
      appBar: widget.appBar,
      body: Row(
        children: <Widget>[
          RailTransition(
            animation: railAnimation,
            backgroundColor: colorScheme.surface,
            child: widget.navigationRail,
          ),
          widget.body,
        ],
      ),
      bottomNavigationBar: BarTransition(
        animation: barAnimation,
        backgroundColor: colorScheme.surface,
        child: widget.navigationBar,
      ),
      // drawer: true ? const NavigationDrawerSection() : null
    );
  }
}

/// 纵向导航栏目的地
final List<NavigationRailDestination> navRailDestinations = appBarDestinations
    .map(
      (destination) => NavigationRailDestination(
        icon: Tooltip(
          message: destination.label,
          child: destination.icon,
        ),
        selectedIcon: Tooltip(
          message: destination.label,
          child: destination.selectedIcon,
        ),
        label: Text(destination.label),
      ),
    )
    .toList();

class SizeAnimation extends CurvedAnimation {
  SizeAnimation(Animation<double> parent)
      : super(
          parent: parent,
          curve: const Interval(
            0.2,
            0.8,
            curve: Curves.easeInOutCubicEmphasized,
          ),
          reverseCurve: Interval(
            0,
            0.2,
            curve: Curves.easeInOutCubicEmphasized.flipped,
          ),
        );
}

class OffsetAnimation extends CurvedAnimation {
  OffsetAnimation(Animation<double> parent)
      : super(
          parent: parent,
          curve: const Interval(
            0.4,
            1.0,
            curve: Curves.easeInOutCubicEmphasized,
          ),
          reverseCurve: Interval(
            0,
            0.2,
            curve: Curves.easeInOutCubicEmphasized.flipped,
          ),
        );
}

/// 左侧垂直导航切换动画
class RailTransition extends StatefulWidget {
  const RailTransition(
      {super.key,
      required this.animation,
      required this.backgroundColor,
      required this.child});

  final Animation<double> animation;
  final Widget child;
  final Color backgroundColor;

  @override
  State<RailTransition> createState() => _RailTransition();
}

class _RailTransition extends State<RailTransition> {
  late Animation<Offset> offsetAnimation;
  late Animation<double> widthAnimation;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();

    // The animations are only rebuilt by this method when the text
    // direction changes because this widget only depends on Directionality.
    final bool ltr = Directionality.of(context) == TextDirection.ltr;

    widthAnimation = Tween<double>(
      begin: 0,
      end: 1,
    ).animate(SizeAnimation(widget.animation));

    offsetAnimation = Tween<Offset>(
      begin: ltr ? const Offset(-1, 0) : const Offset(1, 0),
      end: Offset.zero,
    ).animate(OffsetAnimation(widget.animation));
  }

  @override
  Widget build(BuildContext context) {
    return ClipRect(
      child: DecoratedBox(
        decoration: BoxDecoration(color: widget.backgroundColor),
        child: Align(
          alignment: Alignment.topLeft,
          widthFactor: widthAnimation.value,
          child: FractionalTranslation(
            translation: offsetAnimation.value,
            child: widget.child,
          ),
        ),
      ),
    );
  }
}

///底部导航切换动画
class BarTransition extends StatefulWidget {
  const BarTransition(
      {super.key,
      required this.animation,
      required this.backgroundColor,
      required this.child});

  final Animation<double> animation;
  final Color backgroundColor;
  final Widget child;

  @override
  State<BarTransition> createState() => _BarTransition();
}

class _BarTransition extends State<BarTransition> {
  late final Animation<Offset> offsetAnimation;
  late final Animation<double> heightAnimation;

  @override
  void initState() {
    super.initState();

    offsetAnimation = Tween<Offset>(
      begin: const Offset(0, 1),
      end: Offset.zero,
    ).animate(OffsetAnimation(widget.animation));

    heightAnimation = Tween<double>(
      begin: 0,
      end: 1,
    ).animate(SizeAnimation(widget.animation));
  }

  @override
  Widget build(BuildContext context) {
    return ClipRect(
      child: DecoratedBox(
        decoration: BoxDecoration(color: widget.backgroundColor),
        child: Align(
          alignment: Alignment.topLeft,
          heightFactor: heightAnimation.value,
          child: FractionalTranslation(
            translation: offsetAnimation.value,
            child: widget.child,
          ),
        ),
      ),
    );
  }
}

/// 双列
class OneTwoTransition extends StatefulWidget {
  const OneTwoTransition({
    super.key,
    required this.animation,
    required this.one,
    required this.two,
  });

  final Animation<double> animation;
  final Widget one;
  final Widget two;

  @override
  State<OneTwoTransition> createState() => _OneTwoTransitionState();
}

class _OneTwoTransitionState extends State<OneTwoTransition> {
  late final Animation<Offset> offsetAnimation;
  late final Animation<double> widthAnimation;

  @override
  void initState() {
    super.initState();

    offsetAnimation = Tween<Offset>(
      begin: const Offset(1, 0),
      end: Offset.zero,
    ).animate(OffsetAnimation(widget.animation));

    widthAnimation = Tween<double>(
      begin: 0,
      end: mediumWidthBreakpoint,
    ).animate(SizeAnimation(widget.animation));
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      children: <Widget>[
        Flexible(
          flex: mediumWidthBreakpoint.toInt(),
          child: widget.one,
        ),
        if (widthAnimation.value.toInt() > 0) ...[
          Flexible(
            flex: widthAnimation.value.toInt(),
            child: FractionalTranslation(
              translation: offsetAnimation.value,
              child: widget.two,
            ),
          )
        ],
      ],
    );
  }
}
