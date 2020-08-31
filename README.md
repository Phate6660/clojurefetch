# clojurefetch

I have a problem, ok. I know I do.

This is yet another *fetch program, this time in clojure.

Cause why not.

## output

`$ lein run -- help`

```
general fields:
---------------
D     display device name
d     display distro
e     display editor (requires $EDITOR to be set)
h     display hostname
k     display kernel
s     display shell
U     display user
u     display uptime

help  display help

package counts:
---------------
p     portage (requires qlist until I can figure out globbing)
```

`$ lein run -- DdehkpsUu`

```
Device:    OptiPlex 7010
Distro:    Gentoo
Editor:    /usr/bin/emacsclient
Hostname:  gentoo
Kernel:    5.4.52
Packages   592 (portage)
Shell:     /bin/bash
User:      valley
Uptime:    16h 23m
```
