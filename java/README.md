- DateProp

- custom validation + validation failure
- register each resolver with a registry, once associated

- read a prop and all its layers' values

- ability to mock the response from a specific layer
- connect certain properties with specific layers

- access to set props in some helpers -> ThreadSafety?

Types of props:
- TypedProp (fully spec'd Prop w. dedicated class object, needs a better name)
- GenericProp (built on demand, also typed via subtypes (GenericIntProp, GenericFloatProp, etc.) )
- LayerProp (only available via a single resolver layer)

- Optimized for fast access time
