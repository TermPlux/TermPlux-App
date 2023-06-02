import 'package:flutter/material.dart';

class PlatformCard extends StatelessWidget {
  const PlatformCard(
      {super.key,
      required this.cover,
      required this.title,
      required this.icons,
      required this.pressed});

  final Widget cover;
  final String title;
  final List<Widget> icons;
  final VoidCallback pressed;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final titleStyle = theme.textTheme.headlineSmall!.copyWith(
      color: Colors.white,
    );
    final descriptionStyle = theme.textTheme.titleMedium!;

    return Card(
      clipBehavior: Clip.antiAlias,
      child: InkWell(
        onTap: pressed,
        splashColor: Theme.of(context).colorScheme.onSurface.withOpacity(0.12),
        highlightColor: Colors.transparent,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(
              height: 160,
              child: Stack(
                children: [
                  Positioned.fill(child: cover),
                  Positioned(
                    bottom: 16,
                    left: 16,
                    right: 16,
                    child: FittedBox(
                      fit: BoxFit.scaleDown,
                      alignment: Alignment.centerLeft,
                      child: Semantics(
                        container: true,
                        header: true,
                        child: Text(
                          title,
                          style: titleStyle,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            Semantics(
              container: true,
              child: Padding(
                padding: const EdgeInsets.fromLTRB(16, 16, 16, 0),
                child: DefaultTextStyle(
                  softWrap: false,
                  overflow: TextOverflow.ellipsis,
                  style: descriptionStyle,
                  child: Row(children: icons)
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
