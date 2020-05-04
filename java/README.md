- add Change events to Prop.java and allow users to register to them 

- DateProp (Moment)

- tests


- custom validation + validation failure

- ability to mock the response from a specific layer

- access to set override values for props directly in the registry (for testing) (thread-safe)

Types of props:
- Prop implementation / class (fully spec'd Prop w. dedicated class object, needs a better name)
- SimpleProp (minimal config required to retrieve values and get updates)
- TBD

Goals:
- Optimized for fast access time
