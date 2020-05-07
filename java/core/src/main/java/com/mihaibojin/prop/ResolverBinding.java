package com.mihaibojin.prop;

import com.mihaibojin.resolvers.Resolver;

class ResolverBinding {
  final String id;
  final Resolver resolver;

  ResolverBinding(String id, Resolver resolver) {
    this.id = id;
    this.resolver = resolver;
  }
}
