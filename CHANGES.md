Originally, we had a computeBase() and a computeHash(). In computeHash(), it would reset our message digest and then update it with whatever we got from computeBase(). This was causing small issues in generating the same hash. Thus, we changed it to where we had one method compute the hash. Originally, the purpose of having separate computing was due to the fact we were constantly trying to find a nonce. Instead, we can better search for a nonce if we reset before computing the hash instead of finding the base, updating the digest with the base, and then the nonce. The implementation now just uses computeHash in both constructors. ComputeHash now computes everything at once.