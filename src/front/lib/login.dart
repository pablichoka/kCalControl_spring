import 'package:flutter/material.dart';

class Login extends StatelessWidget {
  final String title = "kCal Control";

  const Login({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          toolbarHeight: 80,
          elevation: 0,
          centerTitle: true,
          title: Text(title, style: Theme.of(context).textTheme.headlineLarge),
          backgroundColor: Colors.transparent,
          flexibleSpace: Container(
            decoration: const BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.topRight,
                end: Alignment.bottomLeft,
                colors: [Colors.blueAccent, Colors.grey, Colors.teal],
              ),
            ),
          ),
        ),
        body: const Column(
          children: [
            Form(

              child: Text('login'),
            ),
          ],
        ));
  }
}

